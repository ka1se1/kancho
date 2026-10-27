package PraAns;

public class Pra11Ans {

	/*
	 * while文を使って12星座を出力するプログラムを作成してください。
	 */
	public static void main(String[] args) {
		// 12星座を配列にする
		String[] seizaList = { "牡羊座", "双子座", "獅子座", "天秤座", "射手座",
				"水瓶座", "牡牛座", "蟹座", "乙女座", "蠍座", "山羊座", "魚座" };

		// while文の実行をカウント + 順番に取り出す用の変数
		int i = 0;

		// while文で一つずつ配列から文字列を取り出す
		while (i < seizaList.length) {
			System.out.println(seizaList[i]);
			// iをカウントしていく
			i = i + 1; // i++でもOK
		}
	}
}
