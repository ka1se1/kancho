import json
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
BLUE, ORANGE, GRAY = "#2a78d6", "#eb6834", "#c9c7c1"
INK, INK2 = "#0b0b0b", "#52514e"

d = json.load(open("results.json", encoding="utf-8"))
res = d["results"]

# 図1: 各方法の平均正解率
keys = list(res.keys())[::-1]
accs = [res[k]["acc"] for k in keys]
best = max(accs)
fig, ax = plt.subplots(figsize=(6.4, 3.8))
bars = ax.barh(keys, accs, color=[BLUE if a == best else GRAY for a in accs],
               height=0.62, edgecolor="white", linewidth=1)
for b, a in zip(bars, accs):
    ax.text(a, b.get_y() + b.get_height() / 2, f" {a:.2f}", va="center",
            ha="left", fontsize=8.5, color=INK2)
ax.set_xlim(0, 1.08)
ax.set_xlabel("平均正解率（10分割交差確認）")
ax.grid(axis="x", color="#e8e7e3", linewidth=0.6)
ax.set_axisbelow(True)
fig.tight_layout()
fig.savefig("fig1_accuracy.png")
plt.close(fig)

# 図2: 特徴ごとの相互情報量（左）と偏りの向き（右：対数オッズ比）
feat = d["feat"]
order = sorted(feat, key=lambda r: r["log_odds"])
words = [f"「{r['word']}」" for r in order]


def color(r):
    if r["mi"] < 0.05:
        return GRAY
    return BLUE if r["log_odds"] > 0 else ORANGE


cols = [color(r) for r in order]
fig, (a1, a2) = plt.subplots(1, 2, figsize=(8.2, 3.3), sharey=True)
b1 = a1.barh(words, [r["mi"] for r in order], color=cols, height=0.62,
             edgecolor="white", linewidth=1)
for b, r in zip(b1, order):
    a1.text(r["mi"], b.get_y() + b.get_height() / 2, f" {r['mi']:.3f}",
            va="center", ha="left", fontsize=8.5, color=INK2)
a1.set_xlim(0, 0.6)
a1.set_title("相互情報量 I(X;Y) [bit]", fontsize=10, color=INK)

b2 = a2.barh(words, [r["log_odds"] for r in order], color=cols, height=0.62,
             edgecolor="white", linewidth=1)
for b, r in zip(b2, order):
    v = r["log_odds"]
    a2.text(v, b.get_y() + b.get_height() / 2, f" {v:+.2f} " if v >= 0 else f"{v:+.2f} ",
            va="center", ha="left" if v >= 0 else "right", fontsize=8.5, color=INK2)
a2.axvline(0, color=INK2, linewidth=0.8)
a2.set_xlim(-7.5, 5)
a2.set_title("対数オッズ比 [log₂]（正＝政治家寄り）", fontsize=10, color=INK)
for a in (a1, a2):
    a.grid(axis="x", color="#e8e7e3", linewidth=0.6)
    a.set_axisbelow(True)
from matplotlib.patches import Patch
fig.legend(handles=[Patch(color=BLUE, label="① 政治家に偏る"),
                    Patch(color=ORANGE, label="② 非政治家に偏る"),
                    Patch(color=GRAY, label="③ 偏らない")],
           loc="lower center", ncol=3, frameon=False, fontsize=9)
fig.tight_layout(rect=(0, 0.08, 1, 1))
fig.savefig("fig2_features.png")
plt.close(fig)
print("ok")
