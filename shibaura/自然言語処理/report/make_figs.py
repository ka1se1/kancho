import csv
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

plt.rcParams.update({
    "font.family": ["Yu Gothic", "Meiryo"],
    "font.size": 10,
    "axes.edgecolor": "#898781",
    "axes.labelcolor": "#52514e",
    "xtick.color": "#52514e",
    "ytick.color": "#52514e",
    "axes.spines.top": False,
    "axes.spines.right": False,
    "figure.dpi": 200,
})
BLUE, ORANGE, AQUA, GRAY = "#2a78d6", "#eb6834", "#1baf7a", "#c9c7c1"
INK, INK2 = "#0b0b0b", "#52514e"


def read(path):
    with open(path, encoding="utf-8") as f:
        return list(csv.DictReader(f))


def label_bars(ax, bars, fmt="{:.2f}", horizontal=False):
    for b in bars:
        v = b.get_width() if horizontal else b.get_height()
        if horizontal:
            ax.text(v, b.get_y() + b.get_height() / 2, " " + fmt.format(v),
                    va="center", ha="left", fontsize=8.5, color=INK2)
        else:
            ax.text(b.get_x() + b.get_width() / 2, v, fmt.format(v),
                    va="bottom", ha="center", fontsize=8.5, color=INK2)


# 図1: 課題1 3ジャンル比較（単語ごとの小さな複数グラフ）
rows = read("task1_data.csv")
genres = ["国会会議録", "教科書", "ブログ"]
words = [("答弁", "国会会議録"), ("図", "教科書"), ("美味しい", "ブログ")]
fig, axes = plt.subplots(1, 3, figsize=(9, 3.0))
for ax, (w, target) in zip(axes, words):
    vals = [float(next(r["pmw"] for r in rows if r["word"] == w and r["subcorpus"] == g)) for g in genres]
    bars = ax.bar(genres, vals, color=[BLUE if g == target else GRAY for g in genres],
                  width=0.6, edgecolor="white", linewidth=1)
    label_bars(ax, bars)
    ax.set_title(f"「{w}」", fontsize=11, color=INK)
    ax.set_ylim(0, max(vals) * 1.18)
    ax.set_ylabel("PMW")
    ax.grid(axis="y", color="#e8e7e3", linewidth=0.6)
    ax.set_axisbelow(True)
fig.tight_layout()
fig.savefig("fig1_task1.png")
plt.close(fig)

# 図2: 「逆転」のサブコーパス別PMW
basic = read("task2_basic.csv")
basic.sort(key=lambda r: float(r["pmw"]))
fig, ax = plt.subplots(figsize=(6.2, 3.4))
names = [r["subcorpus"] for r in basic]
vals = [float(r["pmw"]) for r in basic]
bars = ax.barh(names, vals, color=[BLUE if n in ("ブログ", "雑誌") or n == "出版・雑誌" else GRAY for n in names],
               height=0.65, edgecolor="white", linewidth=1)
label_bars(ax, bars, horizontal=True)
ax.set_xlabel("PMW（100万語あたりの出現回数）")
ax.set_xlim(0, max(vals) * 1.15)
ax.grid(axis="x", color="#e8e7e3", linewidth=0.6)
ax.set_axisbelow(True)
fig.tight_layout()
fig.savefig("fig2_basic.png")
plt.close(fig)

# 図3: 構文パターン頻度（上位12）
pats = read("task2_patterns.csv")
pats = [p for p in pats if p["pattern"] != "(未分類)"][:12][::-1]
fig, ax = plt.subplots(figsize=(6.2, 3.6))
bars = ax.barh([p["pattern"] for p in pats], [int(p["freq"]) for p in pats],
               color=BLUE, height=0.65, edgecolor="white", linewidth=1)
label_bars(ax, bars, fmt="{:.0f}", horizontal=True)
ax.set_xlabel("頻度（件）")
ax.set_xlim(0, 300)
ax.grid(axis="x", color="#e8e7e3", linewidth=0.6)
ax.set_axisbelow(True)
fig.tight_layout()
fig.savefig("fig3_patterns.png")
plt.close(fig)

# 図4: 代表的コロケーションのジャンル別PMW（右パネルに表示のないジャンルは0）
gen4 = ["ブログ", "雑誌", "知恵袋", "書籍(図書館)", "書籍(出版)"]
coll = {
    "逆転勝利": [1.50, 0.65, 0, 0.03, 0.07],
    "大逆転":   [1.58, 1.51, 0.26, 0.29, 0.27],
    "昼夜逆転": [0.33, 0, 0.35, 0.16, 0.14],
}
fig, ax = plt.subplots(figsize=(7.2, 3.2))
w = 0.26
for i, ((name, v), c) in enumerate(zip(coll.items(), [BLUE, ORANGE, AQUA])):
    xs = [x + (i - 1) * w for x in range(len(gen4))]
    bars = ax.bar(xs, v, width=w - 0.03, color=c, label=name)
    for b, val in zip(bars, v):
        if val > 0:
            ax.text(b.get_x() + b.get_width() / 2, val, f"{val:.2f}", ha="center",
                    va="bottom", fontsize=7.5, color=INK2)
ax.set_xticks(range(len(gen4)))
ax.set_xticklabels(gen4)
ax.set_ylabel("PMW")
ax.set_ylim(0, 1.85)
ax.legend(frameon=False, ncol=3, loc="upper right")
ax.grid(axis="y", color="#e8e7e3", linewidth=0.6)
ax.set_axisbelow(True)
fig.tight_layout()
fig.savefig("fig4_colloc.png")
plt.close(fig)
print("ok")
