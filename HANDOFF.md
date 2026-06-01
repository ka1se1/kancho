# カンチョー・ブラスト 3D — 引き継ぎ書

VSCode の Claude (Claude Code) で続きを進めるための資料。
対象ファイル: `kancho-3d.html`（単一ファイル / Three.js r128 を CDN 読み込み）

---

## ⚠️ 最重要：今ファイルは「壊れた状態」です

直前の編集で **新しい強化(combo/xp)・レベル・フィーバー機能を“途中まで”入れた**ため、
**そのまま開いても動きません**（参照先のセーブ項目やUIがまだ無い）。
下の「残タスク」を上から順に終わらせると動くようになります。**安全のため、まず動く状態に戻してから機能追加するのも可**（§6参照）。

---

## 1. これは何のゲーム？

- タイミングバーが中央に来た瞬間にタップ＝カンチョーで人を発射、飛距離を競う。
- 1〜4人ローカル対戦、難易度4種(EASY/NORMAL/HARD/MASTER)。
- コイン経済＋強化ショップ、実績24、デイリーミッション4、フレンド＆ランキング、localStorageセーブ。
- 3D: Three.js。発射された被害者をカメラが追う。効果音(WebAudio合成)＋バイブ。
- 全部入りの単一HTML。スマホ縦持ち前提（最大幅520pxのDOMオーバーレイ + 背面に固定3Dキャンバス）。

座標系: **発射方向 = +X（画面の右へ飛ぶ）/ 上 = +Y / カメラは +Z 側**。
攻撃者 x=-2.4、被害者 x=0 スタート。1ワールド単位 = 1メートル。重力 `GRAV=20`。

---

## 2. ユーザーからの今回の要望（これを実装中）

1. ✅(対応コード投入済) **`toast is not a function` クラッシュを直す** → `toast()`未定義が原因。ヘルパー追加済み。
2. ❌(未) **着地後にキャラが地面に埋まっていくバグを直す**
3. 🚧(着手) **やり込み要素をもっと増やす（何年やっても飽きないように）**
4. 🚧(着手) **見た目の強化要素：あるレベル以上で見た目が豪華になる**
5. 💡 **他に飽きさせない良いアイデアがあれば提案**（§7に提案リストあり。要ユーザー確認）

---

## 3. 直前に投入した“未完成”コード（現状の中身）

### (a) 追加済みヘルパー関数（ファイル中ほど、`haptic()`の直後）
- `toast(msg)` … 画面上部トースト。**`#toast`はHTMLに既存**なので動く。
- `flash(col)` … 画面フラッシュ演出。**`#flash`要素が未作成**（§4-1で追加要）。
- `xpToNext(L)` / `gainXP(amount)` … レベル&XP。**`S.xp` `S.level` `S.stats.maxLevel`が未定義**（§4-2で追加要）。
- `levelDistMul()` … レベルで飛距離+1.2%/Lv（上限なし＝長期成長）。**未だ発射計算に未接続**（§4-4）。
- `comboFrac()` … 強化"combo"のPERFECT連続ボーナス率。**発射計算に未接続**（§4-4）。
- `ZONES` / `zoneIndex(x)` … 飛距離で空・霧を 町→夕焼け→上空→宇宙 に変える定義。**描画に未接続**（§4-5）。
- `TIERS` … レベルで付く見た目(オーラ/王冠/翼/金装飾/天使の輪+虹トレイル)の定義。**キャラ生成に未接続**（§4-6）。
- `updateFeverUI()` / `updateLevelUI()` … **対応DOM(`#fever-fill` `#lv-badge` `#xp-fill` `#xp-text`)が未作成**（§4-1）。

### (b) 拡張済み `SHOP` 配列（max引き上げ＋2種追加）
- power max5→**8**, focus 4→**5**, coin 4→**6**, bounce 3→**4**, wind 4→**6**
- 追加: **combo（コンボマスター, PERFECT連続で飛距離UP, max5）**, **xp（修行のススメ, 獲得XP増加, max5）**
- ⚠️ `DEFAULT_SAVE.upgrades` は **まだ `{power,focus,coin,bounce,wind}` の5個のまま** → combo/xp が無い。§4-2で追加必須。

---

## 4. 残タスク（上から順に実装。これで動く＆要望が満たせる）

### 4-1. HTML/CSS：新UIの追加
ゲーム画面(`<section data-screen="game">`)のHUD付近に以下を追加：
- **レベルバッジ＆XPバー**: `#lv-badge`（"Lv N"）, `#xp-fill`（幅%）, `#xp-text`（"xp / need"）。タイトル画面ヘッダかゲームHUDに。
- **フィーバーゲージ**: `.fever-wrap`(クラス`ready`で光る), `#fever-fill`(幅%), `#fever-label`。
- **画面フラッシュ**: `<div id="flash">` を `#app` 直下あたりに固定配置（`position:fixed;inset:0;pointer-events:none;opacity:0;z-index:3;`）。
CSSは既存のトーンに合わせる（クリーム地+朱色アクセント、Dela Gothic One / Nunito、角丸+3D影）。

### 4-2. セーブのスキーマ拡張（`DEFAULT_SAVE` と `normalize()`）
`DEFAULT_SAVE` に追加:
```js
level:1, xp:0, fever:0,
upgrades:{power:0,focus:0,coin:0,bounce:0,wind:0, combo:0, xp:0},   // combo, xp を追加
stats:{ ... , maxLevel:1, odUses:0, longestCombo:0 }                // 必要に応じ
```
`normalize(d)` 内でも `s.level/s.xp/s.fever` を数値クランプして引き継ぐ処理を追加
（古いセーブには無いので `Number(d.level)||1` 等でフォールバック）。
`upgrades` のループは既存の `for(var k in s.upgrades)` がそのまま combo/xp も拾うのでOK。

### 4-3. 【バグ修正】着地後にキャラが地中に埋まる
原因: `phase==="landed"` の間も `updateGame` 冒頭の `g.vy-=GRAV*dt;` と
`v.position.y+=g.vy*dt;` が回り続け、yがマイナスに潜る。
対策（どちらかで可）:
- (A) 着地確定時に `v.position.y=0` 固定 + `phase==="landed"` の間は物理積分をスキップ
  （`if(g.phase==="flying"){ ...積分... }` で囲い、landed では位置を触らない）。
- (B) 毎フレーム末尾で `if(v.position.y<0) v.position.y=0;` のクランプを必ず入れる。
推奨は(A)。該当は `updateGame()` 内 `if(g.phase==="flying" || g.phase==="landed")` ブロック
（だいたい 1320〜1350行付近、`v.position.x+=g.vx*dt;` などがある所）。
着地ポーズも整える: 倒れて伸びる感じに（`upper.rotation`を寝かせる / 手足だらん）。

### 4-4. 発射計算にレベル＆コンボを接続（やり込みの根幹）
`gameTap()` 内の速度計算（現状）:
```js
var powerMul = upMul("power")*upMul("wind");
var speed = (30 + acc*34)*gp*powerMul;
```
を、レベル倍率とコンボ倍率を掛けるよう変更:
```js
var comboBonus = 1 + comboFrac()*Math.min(S.streak, 10);   // PERFECT連続でじわ伸び
var powerMul = upMul("power")*upMul("wind")*levelDistMul()*comboBonus;
```
（`S.streak` は既存。PERFECTで+1、それ以外で0にリセットされる。）

### 4-5. 飛距離ゾーンで空・霧を変える（背景の変化＝長距離の楽しみ）
`updateGame()` か `updateCamera()` で、被害者の `position.x` から `zoneIndex()` を出し、
`scene.fog.color` と 空ドーム(`world.sky`)のシェーダ uniform `top`/`bottom` を
`ZONES[idx]` の色へ **lerp** で滑らかに変える。
空シェーダの uniform は `world.sky.material.uniforms.top.value` / `.bottom.value`（`THREE.Color`）。
宇宙ゾーン(idx3)では星を出すと良い（点群 or 既存パーティクルの流用）。

### 4-6. レベルで見た目が豪華になる（要望の核心）
`buildHuman()` で作るキャラに装飾用の空グループ（例 `userData.deco`）を持たせ、
プレイヤー（被害者）には現在 `S.level` に応じて `TIERS` の装飾を付ける関数 `applyCosmetics(group, level)` を新設:
- Lv5: 頭上/全身に淡い発光リング（`MeshBasicMaterial` + additive）
- Lv10: 王冠（小さい金のトーラス/円錐の組み合わせ）
- Lv20: 背中に翼（左右対称の薄い板 or 三角の集合）
- Lv25: 金の縁取り（既存メッシュの一部を金マテリアルに差し替え or 追加リング）
- Lv30: 天使の輪（光るトーラス）＋ 飛行トレイルを虹色に（`spawnTrail`の色をHSVで回す）
`startTurn()` で被害者生成後に `applyCosmetics(world.victim, S.level)` を呼ぶ。
注意: r128 に `CapsuleGeometry` は無い（使わない）。トーラス/コーン/板で表現。

### 4-7. フィーバー/オーバードライブ（任意だが飽き防止に強力）
PERFECT/GREATで `S.fever` を加算、満タンで次の一撃が「オーバードライブ」＝
飛距離大幅UP＋演出全開（`flash()` + 強バースト + カメラ強シェイク）。
発射時に消費。`updateFeverUI()` は実装済みなのでUI(§4-1)とロジックを繋ぐだけ。

### 4-8. 実績・ミッションの追加（“何年でも”の担保）
- レベル系実績: Lv10/25/50/100、累計距離10km/100km、プレイ500回、OD○回 等。
- **週替わり/ローテーション ミッション**や、難易度別ベストの実績を足すと長持ち。
- `ACHS` に push、`achProgress()` の `map` にも対応する `[現在値, 目標]` を追加（UIのプログレスバー用）。

### 4-9. レンダリング後の総点検
- §「動作確認」の手順でブラウザ実機テスト（特にスマホ）。
- 構文チェック（§6）。

---

## 5. ファイル構造マップ（行番号は目安。編集でズレるので grep 併用）

```
<style> … 全CSS（モバイル最優先。.screen / .panel / .button / .bar-* / HUD など）
<body>
  #scene(canvas, 3D) / #scrim / #app(最大520px)
    .topbar（BEST/COIN/🔊ミュート）
    main > section.screen[data-screen=...]  ← title/setup/game/result/ranking/shop/friends/achievements
  #toast（既存） / #fatal（THREE読込失敗表示）
<script>（IIFE。THREE未定義なら#fatal表示してreturn）
  - ヘルパー&セーブ: $,clamp,lerp / SAVE_KEY / DIFFS / SHOP / ACHS / MISSIONS / PLAYER_COLORS
  - DEFAULT_SAVE / normalize / loadSave / writeSave / upLv / upMul
  - Audio2（WebAudio合成SFX）/ haptic
  - 【追加済】toast/flash/xpToNext/gainXP/levelDistMul/comboFrac/ZONES/zoneIndex/TIERS/updateFeverUI/updateLevelUI
  - THREE: initThree / buildSky / textures / buildGround / buildScenery / buildClouds / buildMarkers / buildParticlePool / burst / spawnTrail
  - buildHuman / setVictimReady / setVictimFlying
  - 画面遷移: showScreen / renderTop / renderTitle / renderSetup / renderShop / buyUpgrade / renderAch / achProgress / renderMissionList / renderFriends 他
  - ゲーム進行: startGame / startTurn / gameTap / gradeColors / showGradePop / showResult / renderResultActions / showRanking / checkMissions / checkAch / resetSave
  - ループ: update / idleAnim / updateGame / updateNeedles / updateCamera / loop
  - イベント: click委譲 / input / .game-screen pointerdown(=発射) / keydown(Space/Enter) / ピンチ防止
  - 初期化: initThree(); loop(); showScreen("title");
```

主要関数の探し方（grep 推奨）:
`grep -n "function gameTap\|function updateGame\|function buildHuman\|function startTurn\|var DEFAULT_SAVE\|var SHOP =" kancho-3d.html`

---

## 6. 動かす/戻す/チェックの手順

### 構文チェック（Node があれば）
```bash
# インラインJSを抜いて構文確認
python3 - <<'PY'
import re; html=open("kancho-3d.html",encoding="utf-8").read()
js=max(re.findall(r"<script>(.*?)</script>",html,re.S),key=len)
open("_c.js","w",encoding="utf-8").write(js); print(len(js))
PY
node --check _c.js && echo OK; rm -f _c.js
```

### file:// のセキュリティ警告について
コンソールの `Unsafe attempt to load URL file://... 'file:' URLs are treated as unique security origins`
は **localStorage がローカルファイル直開きで弾かれている**ためのもの。コード側は
`rawGet/rawSet` で try/catch し **メモリ保存(memStore)にフォールバック**してあるので
**ゲームは動く**（ただしセーブがページ更新で消える）。きちんとセーブを効かせたいなら
ローカルサーバ経由で開く: `python3 -m http.server` → `http://localhost:8000/kancho-3d.html`。
（このゲーム自体のバグではない。実害は「直開きだとセーブが永続しない」だけ。）

### 一旦“確実に動く版”に戻したい場合
- `kancho-game.html`（旧2D・完成版）が同じフォルダにある。これは単体で動く。
- 3D版を最小修正で動かすだけなら、**§4-1〜4-3 だけ**やれば起動する
  （combo/xp強化やレベル装飾を後回しにしても、SHOPにcombo/xpがあると
   `DEFAULT_SAVE.upgrades`に無くて `upLv` は 0 を返すので**クラッシュはしない**が、
   セーブ拡張§4-2を入れておくのが安全）。

---

## 7. 「飽きさせない」追加アイデア（ユーザー確認の上で）

優先度の高い順。どれを入れるかは Kaito に聞いてから。

1. **マイルストーン報酬**（推奨）: 100m/250m/500m/1km… 初到達でコイン＆装飾アンロック。到達感が出る。
2. **デイリーストリーク**: 連続ログイン日数でボーナス。毎日起動の動機。
3. **チャレンジステージ/天候**: 強風(横風で軌道が曲がる)・夜・雨など、たまに特殊条件。`vari`難易度の発展。
4. **ガチャ/コスメ収集**: コインで見た目（帽子・コスチューム・トレイル色）をランダム or 購入。コレクション欲。
5. **ターゲット/輪っかボーナス**: 空中にリングを置き、通すと加点。狙う面白さ。
6. **リプレイのベスト軌道ゴースト**: 自己ベストの弧を半透明で表示、超える快感。
7. **称号システム**: 累計実績で「カンチョー名人」等の二つ名。プロフィール的satisfaction。
8. **物理イベント**: 建物に当たるとリアクション、看板を割るとコイン、トランポリンで跳ねる等。
9. **BGM**: 今は効果音のみ。簡単なループBGMを WebAudio で。ON/OFF付き。
10. **オンライン要素**（重い）: 本当に飽きさせないなら週替わりランキング等。サーバ要るので最後。

実装軽い順だと **1→2→6→5→3**。まず 1(マイルストーン)＋4(コスメ収集) を入れると
「レベルで豪華に」(§4-6)と噛み合って収集の長期目標になる。

---

## 8. 既知の注意点・ハマりどころ

- Three.js は **r128 / グローバル `THREE`**（モジュール版ではない）。`CapsuleGeometry`不可、`OrbitControls`同梱なし。
- 影は `DirectionalLight` + `shadow.camera` を発射方向(+X)に広めに取っている。装飾を増やすなら `castShadow` の付けすぎに注意（負荷）。
- DOMオーバーレイとCanvasは別レイヤー。**ゲーム中はタップ＝発射**（`.game-screen`の`pointerdown`）。`.no-fire`クラスの要素（やめるボタン等）だけ発射しない。新ボタンを置くなら `no-fire` を付ける。
- 数値は飛距離=メートル。`fmtD()`で "123.4m" 表示。
- セーブキー: `kancho_blast_3d_v1`。スキーマを大きく変えるなら `_v2` にして移行を考えると安全。

---

## 9. 次の一手（このまま Claude Code に投げる用の指示文・例）

> `kancho-3d.html` を開いて。HANDOFF.md の §4 を上から順に実装して。
> まず 4-2(セーブ拡張) → 4-3(着地で埋まるバグ修正) → 4-1(レベル/フィーバー/フラッシュのUI追加)
> → 4-4(発射計算にレベル&コンボ接続) を終わらせて、一度ブラウザで動く状態にして。
> その後 4-5(ゾーンで空が変わる) → 4-6(レベルで見た目が豪華) → 4-7(フィーバー) → 4-8(実績追加) を続けて。
> 各ステップ後に §6 の構文チェックを通すこと。r128にCapsuleGeometryは無いので使わない。
