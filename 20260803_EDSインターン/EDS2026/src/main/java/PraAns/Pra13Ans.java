package PraAns;

public class Pra13Ans {

	/*
	 * 2000年から2100年までの閏（うるう）年を出力する
	 * プログラムを作成してください。
	 * 
	 * 閏年は4で割り切れる年。
	 * ただし400で割り切れない100の倍数年は平年（閏年じゃない）
	 * (例えば1900年、2100年は平年で、2400年は「閏年」です)
	 */
	public static void main(String[] args) {
		// ★範囲を指定する変数を作成する
		int start = 2000;
		int end = 2100;

		System.out.print(start + "年から" + end + "年の閏年：");

		// ★範囲の分、処理を行う
		for (int i = start; i <= end; i++) {

			if (i % 400 == 0) { // 400で割り切れたら閏年
				System.out.print(i + "年、");
			} else if (i % 4 == 0 && i % 100 != 0) { // 4で割り切れ、かつ100で割り切れなかったら閏年
				System.out.print(i + "年、");
			}

		}

	}
}
