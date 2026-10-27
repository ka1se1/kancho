
public class HanteiAns {
	// Hanteiクラス内ではString型の変数「unsei」をフィールドに定義します。
	// publicで直接アクセスしないでprivateにしてgetterで取得する
	private String unsei = "超大凶";

	public String getUnsei() {
		return unsei;
	}

	public void setUnsei(String unsei) {
		this.unsei = unsei;
	}

	// Hanteiクラス内では引数として与えられた数を3で割った
	// 余りによって大吉or中吉or小吉のいずれかを生成し、
	// それをunseiに代入するコンストラクタを作成します。
	HanteiAns(int kuji) {

		// System.out.println("渡された変数：" + kuji);

		// 余りによって運勢を判断する
		// 結果によってunseiに居れる値を更新する
		// せっかくなのでsetterを使う
		if (kuji % 3 == 0) {
			this.setUnsei("大吉");
			// unsei = "大吉";
		} else if (kuji % 3 == 1) {
			this.setUnsei("中吉");
			// unsei = "中吉";
		} else if (kuji % 3 == 2) {
			this.setUnsei("小吉");
			// unsei = "小吉";
		}

	}
}
