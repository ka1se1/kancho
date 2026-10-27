
public class HanteiKAns {

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
	public HanteiKAns(int age, int height, int birth) {

		// 「unseiMoney」「unseiJob」「unseiHealth」
		// それぞれのsetterに
		// 「年齢」「身長」「誕生日」
		// によって運勢を判定する処理を記載します。
		// 金運は年齢から
		setUnseiMoney(age);

		// 仕事運は身長から
		setUnseiJob(height);

		// 健康運は誕生日から
		setUnseiHealth(birth);

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
		// 三段階で判断する
		if (age % 3 == 0) {
			unseiMoney = "大金持ち";
		} else if (age % 3 == 1) {
			unseiMoney = "小金持ち";
		} else if (age % 3 == 2) {
			unseiMoney = "ちょっと貧乏";
		}

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
		// 三段階で判断する
		if (height % 3 == 0) {
			unseiJob = "ビッグチャンス到来";
		} else if (height % 3 == 1) {
			unseiJob = "可もなく不可もなく";
		} else if (height % 3 == 2) {
			unseiJob = "会社が倒産";
		}

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
		// 三段階で判断する
		if (birth % 3 == 0) {
			unseiHealth = "元気もりもり";
		} else if (birth % 3 == 1) {
			unseiHealth = "コロナ感染";
		} else if (birth % 3 == 2) {
			unseiHealth = "ゾンビ化";
		}

		// クラス変数に詰め替える（同じ名前の場合はthisをつける）
		this.unseiHealth = unseiHealth;
	}

}
