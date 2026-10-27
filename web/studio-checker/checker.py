"""スタジオ空室チェッカー。

使い方の例:
    python checker.py                          # 今日、config.json の既定の時間帯
    python checker.py -d 2026-11-03            # 日付指定
    python checker.py -d 2026-11-03 -n 3       # 11/3から3日分
    python checker.py --from 19:00 --to 22:00 --min 2   # 19〜22時の間で2時間以上続けて空いている枠
    python checker.py -s 町田                   # ラベルに「町田」を含む店舗だけ
    python checker.py --rooms 401st 501st      # 部屋名で絞り込み
"""
from __future__ import annotations

import argparse
import json
import sys
import unicodedata
from dataclasses import dataclass
from datetime import date, datetime, timedelta, timezone
from pathlib import Path

from sites import SITES
from sites.base import Slot, hhmm, parse_hhmm

JST = timezone(timedelta(hours=9))
WEEKDAYS = "月火水木金土日"
CONFIG_PATH = Path(__file__).with_name("config.json")


@dataclass
class FreeBlock:
    site: str
    store: str
    room: str
    day: date
    start: int
    end: int

    @property
    def hours(self) -> float:
        return (self.end - self.start) / 60


def collect_slots(stores: list[dict], first_day: date, days: int) -> tuple[list[Slot], list[str]]:
    """config の店舗リストから空き枠を集める。1店舗の失敗で全体を止めない。"""
    slots: list[Slot] = []
    errors: list[str] = []
    for s in stores:
        get = SITES.get(s["site"])
        if get is None:
            errors.append(f"{s['label']}: 未対応のサイト '{s['site']}'")
            continue
        try:
            print(f"取得中: {s['label']} ...", file=sys.stderr)
            slots.extend(get(s["store"], s["label"], first_day, days))
        except Exception as e:
            errors.append(f"{s['label']}: 取得に失敗しました（{e}）")
    return slots, errors


def merge_free_blocks(slots: list[Slot]) -> list[FreeBlock]:
    """連続する空き枠をつなげて「○時〜○時が空き」のかたまりにする。"""
    key = lambda s: (s.store, s.room, s.day)
    blocks: list[FreeBlock] = []
    for s in sorted((s for s in slots if s.available), key=lambda s: (key(s), s.start)):
        last = blocks[-1] if blocks else None
        if last and (last.store, last.room, last.day) == key(s) and last.end == s.start:
            last.end = s.end
        else:
            blocks.append(FreeBlock(s.site, s.store, s.room, s.day, s.start, s.end))
    return blocks


def clip(blocks: list[FreeBlock], win_from: int, win_to: int, min_minutes: int) -> list[FreeBlock]:
    """指定の時間帯に切り詰め、短すぎるものを除く。今日の過ぎた時間も除く。"""
    now = datetime.now(JST)
    out = []
    for b in blocks:
        start, end = max(b.start, win_from), min(b.end, win_to)
        if b.day == now.date():
            start = max(start, now.hour * 60 + now.minute)
            start = -(-start // 30) * 30  # 30分単位に切り上げ
        if end - start >= min_minutes:
            out.append(FreeBlock(b.site, b.store, b.room, b.day, start, end))
    return out


def pad(text: str, width: int) -> str:
    """全角文字を2文字分として数え、表示幅が width になるよう右に空白を足す。"""
    w = sum(2 if unicodedata.east_asian_width(c) in "FW" else 1 for c in text)
    return text + " " * max(width - w, 0)


def print_blocks(blocks: list[FreeBlock], errors: list[str]) -> None:
    if not blocks:
        print("条件に合う空きは見つかりませんでした。")
    cur_day = None
    for b in sorted(blocks, key=lambda b: (b.day, b.store, b.start, b.room)):
        if b.day != cur_day:
            cur_day = b.day
            print(f"\n■ {b.day.month}/{b.day.day}（{WEEKDAYS[b.day.weekday()]}）")
        print(f"  {pad(b.store, 12)} {pad(b.room, 8)} {hhmm(b.start)}〜{hhmm(b.end)}（{b.hours:.1f}h）")
    for e in errors:
        print(f"\n⚠ {e}", file=sys.stderr)


def main() -> None:
    config = json.loads(CONFIG_PATH.read_text(encoding="utf-8"))
    d = config.get("defaults", {})

    p = argparse.ArgumentParser(description="複数のレンタルスタジオの空きをまとめて確認します")
    p.add_argument("-d", "--date", help="日付 YYYY-MM-DD（省略時は今日）")
    p.add_argument("-n", "--days", type=int, default=1, help="何日分見るか（既定: 1）")
    p.add_argument("--from", dest="win_from", default=d.get("from", "00:00"), help="この時刻以降（例 18:00）")
    p.add_argument("--to", dest="win_to", default=d.get("to", "30:00"), help="この時刻まで（例 23:00、深夜は 26:00 など）")
    p.add_argument("--min", type=float, default=d.get("min_hours", 1.0), help="最低何時間続けて空いているか")
    p.add_argument("-s", "--store", help="店舗ラベルの一部で絞り込み（例: 町田）")
    p.add_argument("--rooms", nargs="*", help="部屋名で絞り込み（例: 401st 501st）")
    p.add_argument("--json", action="store_true", help="結果をJSONで出力")
    args = p.parse_args()

    first_day = date.fromisoformat(args.date) if args.date else datetime.now(JST).date()
    stores = [s for s in config["stores"] if not args.store or args.store in s["label"]]
    if not stores:
        sys.exit("該当する店舗が config.json にありません")

    slots, errors = collect_slots(stores, first_day, args.days)

    if args.rooms:
        slots = [s for s in slots if s.room in args.rooms]

    blocks = clip(
        merge_free_blocks(slots),
        parse_hhmm(args.win_from),
        parse_hhmm(args.win_to),
        int(args.min * 60),
    )

    if args.json:
        print(json.dumps(
            [{"store": b.store, "room": b.room, "date": b.day.isoformat(),
              "start": hhmm(b.start), "end": hhmm(b.end), "hours": b.hours} for b in blocks],
            ensure_ascii=False, indent=2,
        ))
        for e in errors:
            print(f"⚠ {e}", file=sys.stderr)
    else:
        print_blocks(blocks, errors)


if __name__ == "__main__":
    main()
