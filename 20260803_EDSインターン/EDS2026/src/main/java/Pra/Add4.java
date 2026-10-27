package Pra;

/*---------------------------------
試験と補習の結果で評価を表示してください。
キーボードからテストの点数を受け付け、int型に変換し int型変数に代入します。 
60点未満の場合には、キーボードから補習受講の有無を ”yes”/”no”で受付け、String型変数に代入します。 
		 【評価】
		 80点以上：		A評価
		 60点以上80点未満：	B評価
		 60点未満：			C評価（補習を受けた）
							D評価（補習を受けていない）
----------------------------------------------------*/
class Add4 {
	public static void main(String[] args) {

		// 変数宣言
		String result;		// 評価結果
		int score;			// 点数
		String addStudy = null;	// 補習を受けた ("yes"/"no")

		// 点数入力
		System.out.print("\n点数を入力してください > ");
		String str = new java.util.Scanner(System.in).nextLine();
		score = Integer.parseInt(str);

		// 60点未満の時に補習の有無を聞く
		if () {
			System.out.print("\n補習を受けましたか？ (yes/no) > ");
			addStudy = new java.util.Scanner(System.in).nextLine();
		}

		// 判定
		if () {
			result = "A評価";
		} else if () {
			result = "B評価";
		} else if ()) {
			result = "C評価";
		} else {
			result = "D評価";
		}

		// 結果の表示
		System.out.println("\n結果：" + result);
	}
}
