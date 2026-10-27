"""LINE公式アカウント（Messaging API）で自分にプッシュ通知を送る。

環境変数に次の2つを設定して使う（GitHub Actions では Secrets に登録）:
    LINE_CHANNEL_ACCESS_TOKEN  … LINE Developers で発行したチャネルアクセストークン（長期）
    LINE_USER_ID               … 自分のユーザーID（U から始まる文字列）

単体で実行するとテスト通知を送る:  python notify.py
"""
from __future__ import annotations

import os
import sys

import requests

PUSH_URL = "https://api.line.me/v2/bot/message/push"
MAX_TEXT = 4900  # 1吹き出しは5000文字まで


def send_line(text: str) -> None:
    token = os.environ.get("LINE_CHANNEL_ACCESS_TOKEN")
    user_id = os.environ.get("LINE_USER_ID")
    if not token or not user_id:
        raise RuntimeError("LINE_CHANNEL_ACCESS_TOKEN と LINE_USER_ID を環境変数に設定してください")
    if len(text) > MAX_TEXT:
        text = text[: MAX_TEXT - 20] + "\n…（以下省略）"
    resp = requests.post(
        PUSH_URL,
        headers={"Authorization": f"Bearer {token}", "Content-Type": "application/json"},
        json={"to": user_id, "messages": [{"type": "text", "text": text}]},
        timeout=20,
    )
    if resp.status_code != 200:
        raise RuntimeError(f"LINE送信に失敗しました: {resp.status_code} {resp.text}")


if __name__ == "__main__":
    send_line("スタジオ空室チェッカーのテスト通知です🎉")
    print("送信しました", file=sys.stderr)
