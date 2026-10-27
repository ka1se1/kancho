"""実際のページと同じ形のサンプルで、読み取り部分が正しく動くか確かめるテスト。
実行: python -m unittest discover tests
"""
import json
import unittest
from datetime import date

from sites import buzz, worcle

TIMES = [f"{h:02d}:{m:02d}" for h in list(range(6, 24)) + list(range(0, 6)) for m in (0, 30)]


def buzz_html():
    info = {"2026-11-01": {t: (1 if t in ("11:00", "11:30") else 0) for t in TIMES}}
    return (
        '<script>var StudioArray = [{"id":397,"name":"B101st"}];\n'
        "var StudioSlotsData = {\n    397: {\n        info: " + json.dumps(info) +
        ',\n        price: {"x": 1},\n        isPack: 0\n    }\n};</script>'
    )


def worcle_html():
    day = "0" * 10 + "1" * 4 + "0" * 16 + "1"  # 31枠: 14:00-16:00 予約、深夜パック予約
    data = {
        "cur_year": 2026, "cur_month": 11, "StartHour": 9,
        "RoomInfo": {"Ids": "57", "Names": "101"},
        "ReserveData": {"D_57": ",".join([day] * 30)},
    }
    return f'<script type="application/json" id="mydata">{json.dumps(data)}</script>'


class TestBuzz(unittest.TestCase):
    def test_parse(self):
        slots = buzz.parse_page(buzz_html(), "BUZZ町田")
        self.assertEqual(len(slots), 48)
        busy = sorted(s.start for s in slots if not s.available)
        self.assertEqual(busy, [660, 690])
        self.assertEqual(max(s.start for s in slots), 29 * 60 + 30)  # 05:30 → 29:30


class TestWorcle(unittest.TestCase):
    def test_parse(self):
        slots = worcle.parse_page(worcle_html(), "worcle新宿")
        d1 = [s for s in slots if s.day == date(2026, 11, 1)]
        self.assertEqual(len(d1), 31)
        self.assertEqual(sorted(s.start for s in d1 if not s.available), [840, 870, 900, 930, 1440])
        self.assertEqual(d1[-1].note, "深夜パック")


if __name__ == "__main__":
    unittest.main()
