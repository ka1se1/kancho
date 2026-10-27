import json
import math

import numpy as np
from sklearn.base import clone
from sklearn.model_selection import StratifiedKFold, cross_val_predict
from sklearn.naive_bayes import BernoulliNB, MultinomialNB
from sklearn.svm import SVC
from sklearn.metrics import confusion_matrix, accuracy_score
from sklearn.pipeline import make_pipeline
from sklearn.preprocessing import Binarizer

FEATS = ["城", "大名", "茶", "明治", "大臣", "戦争", "作品"]


# 疎ベクトルのテキスト表現からベクトルへの変換
def create_vec(s, lo, hi):
    vect = [0] * (hi - lo + 1)
    for item in s.split(" "):
        pos, val = item.split(":")
        vect[int(pos) - lo] = int(val)
    return vect


labels, names, vectors = [], [], []
with open("../person_data.txt", encoding="utf-8") as f:
    for line in f:
        if not line.strip():
            continue
        sp = line.strip().split("\t")
        labels.append(int(sp[0]))
        names.append(sp[1])
        vectors.append(create_vec(sp[2], 1, 7))
X = np.array(vectors)
y = np.array(labels)

# 10分割交差確認（各foldに政治家1人・非政治家1人がテストとして入る）
cv = StratifiedKFold(n_splits=10)
folds = [test.tolist() for _, test in cv.split(X, y)]

models = {
    "BernoulliNB": BernoulliNB(),
    "MultinomialNB": MultinomialNB(),
    "SVM linear C=0.01": SVC(kernel="linear", C=0.01),
    "SVM linear C=0.1": SVC(kernel="linear", C=0.1),
    "SVM linear C=1": SVC(kernel="linear", C=1),
    "SVM linear C=10": SVC(kernel="linear", C=10),
    "SVM RBF C=1": SVC(kernel="rbf", C=1, gamma="scale"),
    "SVM RBF C=10": SVC(kernel="rbf", C=10, gamma="scale"),
    # 出現回数を「出現した(1)/しない(0)」に変換してから線形SVMを適用
    "SVM linear 2値化 C=0.1": make_pipeline(Binarizer(), SVC(kernel="linear", C=0.1)),
    "SVM linear 2値化 C=1": make_pipeline(Binarizer(), SVC(kernel="linear", C=1)),
}

results = {}
for key, model in models.items():
    pred = cross_val_predict(model, X, y, cv=cv)
    # 混同行列：行＝分類器の推測，列＝正解（講義スライドの形式）
    cm = confusion_matrix(y, pred, labels=[1, 2]).T
    fold_acc = [accuracy_score(y[t], pred[t]) for t in folds]
    errors = [names[i] for i in range(len(y)) if pred[i] != y[i]]
    results[key] = {
        "pred": pred.tolist(),
        "cm": cm.tolist(),
        "acc": float(np.mean(fold_acc)),
        "fold_acc": fold_acc,
        "errors": errors,
    }
    print(f"{key:20s} acc={np.mean(fold_acc):.2f} cm={cm.tolist()} err={errors}")

# 誤分類人物ごとのテストfoldと，その時の学習モデルの内部値
fold_of = {i: k for k, t in enumerate(folds) for i in t}
detail = {}
for key in ["BernoulliNB", "SVM linear C=1"]:
    detail[key] = {}
    for i in range(len(y)):
        if results[key]["pred"][i] == y[i]:
            continue
        test = folds[fold_of[i]]
        train = [j for j in range(len(y)) if j not in test]
        m = clone(models[key])
        m.fit(X[train], y[train])
        if key == "BernoulliNB":
            lp = m.predict_log_proba(X[[i]])[0]
            detail[key][names[i]] = {"fold": fold_of[i] + 1,
                                     "logP(政治家)": float(lp[0]), "logP(非政治家)": float(lp[1])}
        else:
            detail[key][names[i]] = {"fold": fold_of[i] + 1,
                                     "decision": float(m.decision_function(X[[i]])[0])}
print(json.dumps(detail, ensure_ascii=False, indent=1))

# 全データで学習した線形SVMの重み（特徴の寄与）とNBの対数尤度比
svm = SVC(kernel="linear", C=1).fit(X, y)
nb = BernoulliNB().fit(X, y)
p = np.exp(nb.feature_log_prob_)  # p(x_i=1 | c)
nb_llr = (np.log(p[0] / p[1])).tolist()  # 出現したときに政治家側へ寄る量
print("svm w (正=非政治家側):", dict(zip(FEATS, svm.coef_[0].round(3).tolist())), "b", svm.intercept_)
print("NB p(x=1|政治家)", dict(zip(FEATS, p[0].round(3).tolist())))
print("NB p(x=1|非政治家)", dict(zip(FEATS, p[1].round(3).tolist())))


# 課題3：特徴分析
def mi(n11, n10, n01, n00):
    # n11: 政治家かつ出現, n10: 政治家かつ非出現, n01: 非政治家かつ出現, n00: 非政治家かつ非出現
    N = n11 + n10 + n01 + n00
    total = 0.0
    for nxy, nx, ny in [(n11, n11 + n01, n11 + n10), (n10, n10 + n00, n11 + n10),
                        (n01, n11 + n01, n01 + n00), (n00, n10 + n00, n01 + n00)]:
        if nxy:
            total += nxy / N * math.log2(N * nxy / (nx * ny))
    return total


feat = []
for k, w in enumerate(FEATS):
    pol = X[y == 1, k]
    non = X[y == 2, k]
    n11, n01 = int((pol > 0).sum()), int((non > 0).sum())
    n10, n00 = 10 - n11, 10 - n01
    s1, s2 = int(pol.sum()), int(non.sum())
    feat.append({
        "word": w, "df_pol": n11, "df_non": n01, "sum_pol": s1, "sum_non": s2,
        "mi": mi(n11, n10, n01, n00),
        # 出現文書数に基づく対数オッズ比（加算スムージング，正なら政治家寄り）
        "log_odds": math.log2(((n11 + 0.5) / (n10 + 0.5)) / ((n01 + 0.5) / (n00 + 0.5))),
        "share_pol": s1 / (s1 + s2),
    })
for r in feat:
    print(r)

json.dump({"names": names, "labels": labels, "X": X.tolist(), "folds": folds,
           "results": results, "detail": detail, "feat": feat,
           "svm_w": svm.coef_[0].tolist(), "svm_b": float(svm.intercept_[0]),
           "nb_p": p.tolist(), "nb_llr": nb_llr},
          open("results.json", "w", encoding="utf-8"), ensure_ascii=False, indent=1)
