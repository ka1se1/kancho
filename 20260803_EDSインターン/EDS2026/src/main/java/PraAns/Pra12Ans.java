package PraAns;

public class Pra12Ans {

	/*
	 * 変数a,bを作成し、aとbの公倍数を6個表示する
	 * プログラムを作成してください。
	 */
	public static void main(String[] args) {
		// ★変数a,bを作成する
		int a = 8;
		int b = 9;
		// 公倍数のカウント用変数
		int c = 0;
		// 表示する公倍数用の変数　※今回は6
		int z = 6;
		// カウントアップ用変数（0は公倍数とはしないので1から）
		int d = 1;
		System.out.print(a + "と" + b + "の公倍数は");
		// 公倍数が6つになるまで出力
		while (c < z) { // ★while文で公倍数が6になるまで実行
			// ★公倍数を判定する処理
			if (d % a == 0 && d % b == 0) {
				// 両方で割り切れるため公倍数として出力
				System.out.print(d);
				// 公倍数のカウントを一つ増やす
				c = c + 1; // c++でもOK

				// 6つ目の数字の後には「,」をつけないようにする
				if (c < z) {
					System.out.print(",");
				}
			}
			// カウントアップする
			d = d + 1; // d++でもOK
		}
		System.out.println("です。");
	}
}
