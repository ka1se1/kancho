"""全サイト共通のデータ形式とHTTPヘルパー。"""
from __future__ import annotations

import time
from dataclasses import dataclass
from datetime import date

import requests

# 普通のブラウザに近いヘッダー。これがないと弾くサイトがある
HEADERS = {
    "User-Agent": (
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
        "(KHTML, like Gecko) Chrome/130.0 Safari/537.36"
    ),
    "Accept-Language": "ja,en;q=0.8",
}

# 同じサイトへの連続アクセスの間隔（秒）。サイトに負荷をかけないため
REQUEST_INTERVAL = 1.5
_last_request = 0.0


def fetch(url: str) -> str:
    """URLのHTMLを取得する。アクセス間隔を自動で空ける。"""
    global _last_request
    wait = REQUEST_INTERVAL - (time.time() - _last_request)
    if wait > 0:
        time.sleep(wait)
    resp = requests.get(url, headers=HEADERS, timeout=20)
    _last_request = time.time()
    resp.raise_for_status()
    resp.encoding = resp.apparent_encoding if resp.encoding is None else resp.encoding
    return resp.text


@dataclass
class Slot:
    """1つの時間枠。start/end はその日の0:00からの分（24時以降は1440以上）。"""

    site: str        # "BUZZ" / "worcle" など
    store: str       # 店舗名
    room: str        # 部屋名
    day: date        # 利用日
    start: int       # 開始（分）
    end: int         # 終了（分）
    available: bool  # True=空き
    note: str = ""   # 「深夜パック」など補足


def hhmm(minutes: int) -> str:
    """分を "HH:MM" に。24時以降は 25:00 のように表記する。"""
    return f"{minutes // 60:02d}:{minutes % 60:02d}"


def parse_hhmm(text: str) -> int:
    h, m = text.split(":")
    return int(h) * 60 + int(m)
