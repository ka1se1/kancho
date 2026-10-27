"""サイトごとの読み取りモジュールの登録場所。

新しいサイトを足すときは sites/<名前>.py に get_slots(store, label, first_day, days) を作り、
下の SITES に1行追加する。
"""
from . import buzz, worcle

SITES = {
    "buzz": buzz.get_slots,
    "worcle": worcle.get_slots,
}
