"""BUZZ（buzz-st.com）の空室取得。

予約表ページ https://buzz-st.com/<店舗>/<YYYY-MM-DD> のHTMLに、
指定日から7日分の空き状況がJavaScriptの変数として埋め込まれている:

    StudioArray = [{"id":397,"name":"B101st"}, ...];
    StudioSlotsData = {
        397: { info: {"2026-11-01": {"06:00":0, "06:30":1, ...}, ...}, price: ..., ... },
        ...
    }

0 = 空き、1 = 予約済み。時刻は 06:00〜23:30 のあと 00:00〜05:30（翌朝）と並ぶ。
"""
from __future__ import annotations

import json
import re
from datetime import date, timedelta

from .base import Slot, fetch, parse_hhmm

BASE_URL = "https://buzz-st.com"
DAYS_PER_PAGE = 7
DAY_START = 6 * 60  # BUZZの1日は6:00始まり。これより前の時刻は「翌朝」扱い


def _extract_json_object(text: str, start: int) -> tuple[object, int]:
    """text[start] から始まる {...} を括弧の対応で切り出して JSON として読む。"""
    depth = 0
    in_str = False
    escape = False
    for i in range(start, len(text)):
        ch = text[i]
        if in_str:
            if escape:
                escape = False
            elif ch == "\\":
                escape = True
            elif ch == '"':
                in_str = False
            continue
        if ch == '"':
            in_str = True
        elif ch == "{":
            depth += 1
        elif ch == "}":
            depth -= 1
            if depth == 0:
                return json.loads(text[start : i + 1]), i + 1
    raise ValueError("JSONオブジェクトの終わりが見つかりません")


def parse_page(html: str, store_label: str) -> list[Slot]:
    """予約表ページのHTMLから Slot のリストを作る。"""
    m = re.search(r"StudioArray\s*=\s*(\[.*?\])\s*;", html, re.S)
    if not m:
        raise ValueError("StudioArray が見つかりません（ページの仕組みが変わった可能性があります）")
    room_names = {str(r["id"]): r["name"] for r in json.loads(m.group(1))}

    pos = html.find("StudioSlotsData")
    if pos < 0:
        raise ValueError("StudioSlotsData が見つかりません（ページの仕組みが変わった可能性があります）")
    body = html[pos:]

    slots: list[Slot] = []
    for rm in re.finditer(r"(\d+)\s*:\s*\{\s*info\s*:\s*", body):
        room_id = rm.group(1)
        if room_id not in room_names:
            continue
        info, _ = _extract_json_object(body, rm.end())
        for day_str, times in info.items():
            day = date.fromisoformat(day_str)
            for t, status in times.items():
                start = parse_hhmm(t)
                if start < DAY_START:  # 00:00〜05:59 は翌朝なので 24:00〜29:59 として扱う
                    start += 24 * 60
                slots.append(
                    Slot(
                        site="BUZZ",
                        store=store_label,
                        room=room_names[room_id],
                        day=day,
                        start=start,
                        end=start + 30,
                        available=(status == 0),
                        note="深夜" if start >= 23 * 60 + 30 else "",
                    )
                )
    if not slots:
        raise ValueError("空き状況のデータを読み取れませんでした")
    return slots


def get_slots(store: str, label: str, first_day: date, days: int) -> list[Slot]:
    """first_day から days 日分の Slot を取得する。1ページ7日分なので必要な分だけアクセスする。"""
    result: list[Slot] = []
    last_day = first_day + timedelta(days=days - 1)
    cur = first_day
    while cur <= last_day:
        html = fetch(f"{BASE_URL}/{store}/{cur.isoformat()}")
        result.extend(s for s in parse_page(html, label) if first_day <= s.day <= last_day)
        cur += timedelta(days=DAYS_PER_PAGE)
    return result
