# スタジオ空室チェッカー

BUZZ と studio worcle の空き状況をまとめて取得し、「○時〜○時が続けて空いている部屋」を一覧にするツールです。
条件に合う空きが新しく出たときに LINE へ通知する**監視モード**もあり、GitHub Actions で30分おきに自動実行できます。

```
> python checker.py -d 2026-11-03 --from 20:00 --to 23:00 --min 2

■ 11/3（火）
  BUZZ町田     401st    20:00〜23:00（3.0h）
  BUZZ町田     B101st   21:00〜23:00（2.0h）
  BUZZ相模原   スタジオ 21:30〜23:00（1.5h）
  worcle新宿   202      21:00〜23:00（2.0h）
  worcle渋谷   106      20:30〜23:00（2.5h）
```

## 目次

- [準備](#準備最初の1回だけ)
- [空き状況を調べる（checker.py）](#空き状況を調べるcheckerpy)
- [config.json の書き方](#configjson-の書き方)
- [LINE通知（監視モード / watch.py）](#line通知監視モード--watchpy)
- [新しいサイトの追加](#新しいサイトの追加)
- [仕組みとメモ](#仕組みとメモ)
- [困ったとき](#困ったとき)

## ファイル構成

```
studio-checker/
├── checker.py              空き状況を調べて表示する（手動で使う）
├── watch.py                監視モード。新しい空きをLINEに通知する
├── notify.py               LINE送信。単体で実行するとテスト通知
├── config.json             店舗・既定の時間帯・監視条件
├── state.json              通知済みの記録（watch.py が自動で作る）
├── sites/
│   ├── __init__.py         サイトの登録（SITES）
│   ├── base.py             共通のデータ形式（Slot）とHTTP取得
│   ├── buzz.py             BUZZ の読み取り
│   └── worcle.py           studio worcle の読み取り
├── tests/test_parsers.py   読み取り部分のテスト
└── .github/workflows/watch.yml   GitHub Actions の定期実行
```

## 準備（最初の1回だけ）

Python 3.9 以上（GitHub Actions では 3.12）が必要です。

```
pip install -r requirements.txt
```

## 空き状況を調べる（checker.py）

```
python checker.py                                   # 今日・config.json の既定の時間帯
python checker.py -d 2026-11-03                     # 日付を指定
python checker.py -d 2026-11-03 -n 3                # 11/3から3日分
python checker.py --from 19:00 --to 22:00 --min 2   # 19〜22時に2時間以上続けて空き
python checker.py -s 町田                            # ラベルに「町田」を含む店舗だけ
python checker.py --rooms 401st 501st               # 部屋を絞る
python checker.py --to 30:00                        # 深夜（翌6時）まで含める
python checker.py --json                            # 結果をJSONで出力
```

| オプション | 意味 | 省略したとき |
|---|---|---|
| `-d`, `--date` | 最初の日付（`YYYY-MM-DD`） | 今日 |
| `-n`, `--days` | 何日分見るか | 1 |
| `--from` | この時刻以降 | `defaults.from` |
| `--to` | この時刻まで。深夜は `26:00`（翌2時）のように24以上で書く | `defaults.to` |
| `--min` | 最低何時間続けて空いているか（`1.5` なども可） | `defaults.min_hours` |
| `-s`, `--store` | 店舗ラベルの一部で絞り込み | 全店舗 |
| `--rooms` | 部屋名で絞り込み（スペース区切りで複数） | 全部屋 |
| `--json` | 結果をJSONで出力 | 表形式 |

- 今日の分は、今の時刻より前の枠を除いて表示します（30分単位に切り上げ）。
- 部屋名はサイトの表記そのままです（BUZZ は `401st` など、worcle は `101` など。BUZZ相模原は `スタジオ` の1部屋）。
- 1店舗の取得に失敗しても他の店舗は表示し、最後に `⚠` でエラーを出します。

## config.json の書き方

```json
{
  "stores": [
    { "site": "buzz",   "store": "machida",  "label": "BUZZ町田" },
    { "site": "worcle", "store": "shinjuku", "label": "worcle新宿" }
  ],
  "defaults": { "from": "18:00", "to": "23:00", "min_hours": 1.0 },
  "watches": [ ... ]
}
```

### stores（店舗）

| キー | 内容 |
|---|---|
| `site` | `buzz` または `worcle`（`sites/__init__.py` の `SITES` に登録した名前） |
| `store` | 各サイトのURLの店舗部分 |
| `label` | 表示名。`-s` の絞り込みや `watches` の `stores` でもこの名前を使う |

`store` はURLから取ります。

- BUZZ：`https://buzz-st.com/machida` → `"store": "machida"`
- worcle：`https://www.studioworcle.com/shinjuku/` → `"store": "shinjuku"`

### defaults（checker.py の既定値）

`from` / `to` / `min_hours` を書くと、オプションを省略したときにこの値を使います。

### watches（監視条件）

次の「LINE通知」で使います。

---

## LINE通知（監視モード / watch.py）

`config.json` の `watches` に書いた条件に合う空きが**新しく出たときだけ**、LINEに1通にまとめて通知します。
通知には各店舗の予約ページへのリンクが付きます。

一度通知した「日付・条件・店舗・部屋」は `state.json` に記録され、その部屋が埋まって再び空くまでは再通知しません。
（LINE公式アカウントの無料プランは月200通までなので、送る回数をなるべく抑えています）

### 監視条件の書き方

```json
"watches": [
  { "name": "土日の練習", "weekdays": ["土", "日"], "days_ahead": 14,
    "from": "18:00", "to": "22:00", "min_hours": 2,
    "stores": ["BUZZ町田", "BUZZ相模原"] },

  { "name": "11/8 チーム練", "dates": ["2026-11-08"],
    "from": "13:00", "to": "20:00", "min_hours": 3,
    "stores": ["BUZZ町田", "worcle新宿"], "rooms": ["401st", "501st", "401", "501"] }
]
```

| キー | 内容 | 省略したとき |
|---|---|---|
| `name` | 条件の名前（通知に表示される） | 空欄 |
| `dates` | 対象日を直接指定（`YYYY-MM-DD` のリスト）。書くと `weekdays` / `days_ahead` は無視 | — |
| `weekdays` | 対象の曜日（`"月"`〜`"日"`） | 全曜日 |
| `days_ahead` | 今日から何日先まで見るか | 14 |
| `from` / `to` | 時間帯 | `00:00` / `30:00` |
| `min_hours` | 最低何時間続けて空いているか | 1 |
| `stores` | 対象店舗（`stores` の `label`） | 全店舗 |
| `rooms` | 対象の部屋名 | 全部屋 |

### 1. LINE公式アカウントの準備

1. LINE Official Account Manager でLINE公式アカウントを作成（無料のコミュニケーションプランでOK）
2. アカウントの「設定」→「Messaging API」→「Messaging APIを利用する」
3. LINE Developers コンソールでそのチャネルを開く
   - 「Messaging API設定」タブ → **チャネルアクセストークン（長期）** を発行してコピー
   - 「チャネル基本設定」タブの下の方 → **あなたのユーザーID**（Uから始まる）をコピー
4. 「Messaging API設定」タブのQRコードを読み取って、自分のLINEで友だち追加

### 2. 手元で試す

トークンとユーザーIDを環境変数に入れて、テスト通知を送ります（PowerShell の例）。

```powershell
$env:LINE_CHANNEL_ACCESS_TOKEN = "（1-3 のトークン）"
$env:LINE_USER_ID = "（1-3 のユーザーID）"
python notify.py              # 「テスト通知です🎉」が届けばOK
python watch.py --dry-run     # 通知せず、送る予定の内容を表示
python watch.py               # 実際に通知し、state.json を更新
```

初回は `state.json` が空なので、条件に合う空きが全部まとめて届きます。部屋数が多いと長くなり、
LINEの文字数上限（約5000字）を超えた分は「…（以下省略）」で切られます。多すぎる場合は `rooms` や `min_hours` で条件を絞ってください。

`--dry-run` は環境変数がなくても動きます。手元で `python watch.py` を実行すると `state.json` が書き換わるので、
GitHub Actions と併用する場合は手元では `--dry-run` だけにしておくと記録がずれません。

### 3. GitHub Actions で30分おきに実行

1. GitHubで **Private** リポジトリを作り、このフォルダの中身を全部アップロード（`.github` フォルダも含む）
2. リポジトリの Settings → Secrets and variables → Actions → New repository secret で2つ登録
   - `LINE_CHANNEL_ACCESS_TOKEN` … 1-3 のトークン
   - `LINE_USER_ID` … 1-3 のユーザーID
3. Actions タブ → 「studio watch」→ 「Run workflow」で手動実行して、LINEに届くか確認
4. 以降は30分おきに自動で動きます（GitHub側の都合で数分〜十数分遅れることがあります）

- 実行のたびに、通知済みの記録 `state.json` が「update state」というコミットでリポジトリに保存されます。
- 全店舗の取得に失敗したときは実行が失敗扱いになり、GitHub からメールが届きます。
- iPhoneの GitHub アプリからも Actions の手動実行ができるので、「今すぐ確認したい」ときに使えます。
- 通知を止めたいときは Actions タブで「Disable workflow」。
- 監視条件を変えたいときは、GitHub 上で `config.json` を編集してコミットすれば次の実行から反映されます。

## 新しいサイトの追加

1. `sites/<サイト名>.py` を作り、`get_slots(store, label, first_day, days)` で `Slot` のリストを返す関数を書く
   - `Slot` は30分などの1枠ごとに作り、`start` / `end` はその日の0:00からの分（24時以降は1440以上）
2. `sites/__init__.py` の `SITES` に1行追加する
3. `config.json` の `stores` に `"site": "<サイト名>"` の店舗を足す
4. 通知に予約リンクを付けたい場合は `watch.py` の `reserve_link` にも追加する

読み取り部分のテストは次で実行できます。

```
python -m unittest discover tests
```

## 仕組みとメモ

- **BUZZ**：予約表ページ（`/<店舗>/<日付>`）に7日分の空き状況（0=空き/1=予約済み）が埋め込まれているのを読んでいます。
  1日は6:00始まりで、00:00〜05:30 は翌朝（24:00〜29:30）として扱います。
- **worcle**：店舗ページ（`/<店舗>/?q=YYMM01`）に1か月分の空き状況が `mydata` というJSONで埋め込まれているのを読んでいます。
  最後の枠は深夜パック（24:00〜30:00）です。
- 必要な日数・月数の分だけページを取得します（BUZZ は7日ごと、worcle は1か月ごとに1回）。
- どちらもアクセスは1.5秒以上あけています。定期実行する場合も数十分に1回程度にしてください。

## 困ったとき

| 症状 | 原因と対処 |
|---|---|
| `○○が見つかりません（ページの仕組みが変わった可能性があります）` | サイトの作りが変わった。`sites/` の読み取り部分の修正が必要 |
| `取得に失敗しました（403 ...）` など | サイト側でアクセスを弾いている。時間をおいて再実行する |
| `ModuleNotFoundError: No module named 'requests'` | `pip install -r requirements.txt` を実行する |
| `LINE_CHANNEL_ACCESS_TOKEN と LINE_USER_ID を環境変数に設定してください` | 環境変数（Actions なら Secrets）が未設定 |
| `LINE送信に失敗しました: 401` | トークンが間違っている／再発行で無効になった |
| `watches の店舗 '○○' が stores にありません` | `watches` の `stores` と `stores` の `label` の表記を揃える |
| 文字化けする（Git Bash など） | `python -X utf8 checker.py` のように `-X utf8` を付ける |
| 通知が来ない | `python watch.py --dry-run` で条件に合う空きがあるか確認。すでに通知済みなら `state.json` に記録されている |
