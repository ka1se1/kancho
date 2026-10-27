"""監視モード。config.json の "watches" の条件に合う空きが「新しく」出たらLINEに通知する。

    python watch.py            # チェックして、新しい空きがあれば通知
    python watch.py --dry-run  # 通知せず、送る予定の内容を表示するだけ

一度通知した枠は state.json に記録し、同じ枠で何度も通知しない。
（無料プランは月200通までなので、新しい空きが出たときだけ1通にまとめて送る）
"""
from __future__ import annotations

import argparse
import json
import sys
from datetime import date, datetime, timedelta
from pathlib import Path

from checker import CONFIG_PATH, JST, WEEKDAYS, FreeBlock, clip, collect_slots, merge_free_blocks
from sites.base import hhmm, parse_hhmm

STATE_PATH = Path(__file__).with_name("state.json")


def target_dates(w: dict, today: date) -> list[date]:
    """監視条件から対象日のリストを作る。dates 指定 か weekdays + days_ahead 指定。"""
    if "dates" in w:
        return sorted(d for d in map(date.fromisoformat, w["dates"]) if d >= today)
    ahead = int(w.get("days_ahead", 14))
    days = [today + timedelta(days=i) for i in range(ahead)]
    if "weekdays" in w:
        days = [d for d in days if WEEKDAYS[d.weekday()] in w["weekdays"]]
    return days


def reserve_link(store_conf: dict, day: date) -> str:
    if store_conf["site"] == "buzz":
        return f"https://buzz-st.com/{store_conf['store']}/{day.isoformat()}"
    if store_conf["site"] == "worcle":
        return f"https://www.studioworcle.com/{store_conf['store']}/?q={day:%y%m}01"
    return ""


def block_key(name: str, b: FreeBlock) -> str:
    """通知の単位は「日付・条件・店舗・部屋」。空き時間が少し変わっただけでは再通知しない。"""
    return f"{b.day.isoformat()}|{name}|{b.store}|{b.room}"


def load_state(today: date) -> set[str]:
    if not STATE_PATH.exists():
        return set()
    keys = json.loads(STATE_PATH.read_text(encoding="utf-8")).get("notified", [])
    return {k for k in keys if date.fromisoformat(k.split("|")[0]) >= today}  # 過去の日付は捨てる


def save_state(keys: set[str]) -> None:
    STATE_PATH.write_text(json.dumps({"notified": sorted(keys)}, ensure_ascii=False, indent=1), encoding="utf-8")


def main() -> None:
    p = argparse.ArgumentParser()
    p.add_argument("--dry-run", action="store_true", help="通知せず内容を表示するだけ")
    args = p.parse_args()

    config = json.loads(CONFIG_PATH.read_text(encoding="utf-8"))
    watches = config.get("watches", [])
    if not watches:
        sys.exit('config.json に "watches" がありません')
    stores_by_label = {s["label"]: s for s in config["stores"]}
    today = datetime.now(JST).date()

    # 各監視条件の対象日をまとめ、必要な店舗だけ・必要な期間だけ取得する
    plans = []
    need: dict[str, set[date]] = {}
    for w in watches:
        dates = target_dates(w, today)
        labels = w.get("stores") or list(stores_by_label)
        plans.append((w, dates, labels))
        for lb in labels:
            if lb not in stores_by_label:
                sys.exit(f"watches の店舗 '{lb}' が stores にありません")
            need.setdefault(lb, set()).update(dates)

    slots, errors = [], []
    for lb, dates in need.items():
        if not dates:
            continue
        first, last = min(dates), max(dates)
        got, errs = collect_slots([stores_by_label[lb]], first, (last - first).days + 1)
        slots.extend(got)
        errors.extend(errs)

    free = merge_free_blocks(slots)
    notified = load_state(today)
    found: dict[str, tuple[str, list[FreeBlock]]] = {}
    for w, dates, labels in plans:
        cand = [
            b for b in free
            if b.day in dates and b.store in labels and (not w.get("rooms") or b.room in w["rooms"])
        ]
        for b in clip(cand, parse_hhmm(w.get("from", "00:00")), parse_hhmm(w.get("to", "30:00")),
                      int(float(w.get("min_hours", 1)) * 60)):
            name = w.get("name", "")
            found.setdefault(block_key(name, b), (name, []))[1].append(b)

    new = {k: v for k, v in found.items() if k not in notified}
    if new:
        lines = ["🎉 条件に合う空きが出ました"]
        for k in sorted(new):
            name, blocks = new[k]
            b = blocks[0]
            times = "、".join(f"{hhmm(x.start)}〜{hhmm(x.end)}（{x.hours:.1f}h）" for x in blocks)
            lines.append(
                f"\n【{name}】\n{b.day.month}/{b.day.day}（{WEEKDAYS[b.day.weekday()]}）"
                f"{b.store} {b.room}\n{times}\n"
                f"{reserve_link(stores_by_label[b.store], b.day)}"
            )
        text = "\n".join(lines)
        if args.dry_run:
            print(text)
        else:
            from notify import send_line
            send_line(text)
            print(f"{len(new)}件を通知しました", file=sys.stderr)
    else:
        print("新しい空きはありません", file=sys.stderr)

    if not args.dry_run:
        # 今も空いている通知済みの枠だけ残す。埋まって再び空いたら、また通知される
        save_state((notified & set(found)) | set(new))

    for e in errors:
        print(f"⚠ {e}", file=sys.stderr)
    if errors and not slots:
        sys.exit(1)  # 全店舗失敗したらエラー終了（GitHub Actions からメールが届く）


if __name__ == "__main__":
    main()
