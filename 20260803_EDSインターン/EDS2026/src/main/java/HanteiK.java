
public class HanteiK {

	// HanteiKクラス内ではString型の変数
	//「unseiMoney」「unseiJob」「unseiHealth」
	// をフィールドに定義します。
	String unseiMoney = "";
	String unseiJob = "";
	String unseiHealth = "";

	// HanteiKクラス内では引数として与えられた
	// 3つの数をそれぞれ3で割った余りによって
	// 「金運」「仕事運」「健康運」の各運勢を生成し、
	// それを「unseiMoney」「unseiJob」「unseiHealth」
	// に代入するコンストラクタを作成します。
	public HanteiK(int age, int height, int birth) {

		// 「unseiMoney」「unseiJob」「unseiHealth」
		// それぞれのsetterに
		// 「年齢」「身長」「誕生日」
		// によって運勢を判定する処理を記載します。
		// 金運は年齢から
		setUnseiMoney(age);

		// 仕事運は身長から
		// ★setUnseiMoneyを参考に仕事運を身長から設定

		// 健康運は誕生日から
		// ★setUnseiMoneyを参考に健康運を誕生日から設定
	}

	/**
	 * @return unseiMoney
	 */
	public String getUnseiMoney() {
		return unseiMoney;
	}

	/**
	 * @param age セットする unseiMoney
	 */
	public void setUnseiMoney(int age) {

		String unseiMoney = "";

		// ★引数ageから金運を判定する処理

		// クラス変数に詰め替える（同じ名前の場合はthisをつける）
		this.unseiMoney = unseiMoney;
	}

	/**
	 * @return unseiJob
	 */
	public String getUnseiJob() {
		return unseiJob;
	}

	/**
	 * @param height セットする unseiJob
	 */
	public void setUnseiJob(int height) {

		String unseiJob = "";

		// ★引数heightから仕事運を判定する処理

		// クラス変数に詰め替える（同じ名前の場合はthisをつける）
		this.unseiJob = unseiJob;
	}

	/**
	 * @return unseiHealth
	 */
	public String getUnseiHealth() {
		return unseiHealth;
	}

	/**
	 * @param birth セットする unseiHealth
	 */
	public void setUnseiHealth(int birth) {

		String unseiHealth = "";

		// ★引数birthから健康運を判定する処理

		// クラス変数に詰め替える（同じ名前の場合はthisをつける）
		this.unseiHealth = unseiHealth;
	}

}
