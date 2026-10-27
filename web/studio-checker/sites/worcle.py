"""studio worcle（studioworcle.com）の空室取得。

店舗ページ https://www.studioworcle.com/<店舗>/?q=YYMM01 のHTMLに、
1か月分の空き状況が <script id="mydata" type="application/json"> として埋め込まれている:

    RoomInfo:    {"Ids": "57,58,...", "Names": "101,102,...", ...}
    ReserveData: {"D_57": "0001111...,0000111...,...", ...}   # 部屋ごと・日ごと（カンマ区切り）
    StartHour:   9

1日分の文字列は1文字が30分枠で、StartHour から 24:00 まで続き、
最後の1文字が深夜パック（24:00〜30:00）。0 = 空き、1 = 予約済み（または営業時間外）。
"""
from __future__ import annotations

import json
import re
from datetime import date, timedelta

from .base import Slot, fetch

BASE_URL = "https://www.studioworcle.com"


def parse_page(html: str, store_label: str) -> list[Slot]:
    m = re.search(r'<script[^>]*id="mydata"[^>]*>(.*?)</script>', html, re.S)
    if not m:
        raise ValueError("予約データ(mydata)が見つかりません（ページの仕組みが変わった可能性があります）")
    data = json.loads(m.group(1))

    year, month = int(data["cur_year"]), int(data["cur_month"])
    start_hour = int(data["StartHour"])
    ids = data["RoomInfo"]["Ids"].split(",")
    names = data["RoomInfo"]["Names"].split(",")
    normal_slots = (24 - start_hour) * 2  # StartHour〜24:00 の30分枠の数

    slots: list[Slot] = []
    for room_id, room_name in zip(ids, names):
        per_day = data["ReserveData"].get(f"D_{room_id}")
        if not per_day:
            continue
        for day_index, bits in enumerate(per_day.split(",")):
            day = date(year, month, day_index + 1)
            for i, bit in enumerate(bits):
                if i < normal_slots:
                    start = start_hour * 60 + i * 30
                    end, note = start + 30, ""
                else:
                    start, end, note = 24 * 60, 30 * 60, "深夜パック"
                slots.append(
                    Slot(
                        site="worcle",
                        store=store_label,
                        room=room_name,
                        day=day,
                        start=start,
                        end=end,
                        available=(bit == "0"),
                        note=note,
                    )
                )
    if not slots:
        raise ValueError("空き状況のデータを読み取れませんでした")
    return slots


def get_slots(store: str, label: str, first_day: date, days: int) -> list[Slot]:
    """first_day から days 日分。1ページ1か月分なので、またがる月の数だけアクセスする。"""
    last_day = first_day + timedelta(days=days - 1)
    months = []
    cur = first_day.replace(day=1)
    while cur <= last_day:
        months.append(cur)
        cur = (cur + timedelta(days=32)).replace(day=1)

    result: list[Slot] = []
    for mon in months:
        q = f"{mon.year % 100:02d}{mon.month:02d}01"
        html = fetch(f"{BASE_URL}/{store}/?q={q}")
        result.extend(s for s in parse_page(html, label) if first_day <= s.day <= last_day)
    return result
