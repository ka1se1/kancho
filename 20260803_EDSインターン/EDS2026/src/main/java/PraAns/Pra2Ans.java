package PraAns;

public class Pra2Ans {

	/*
	 * 【練習２】　if文を使って0と12の四則演算結果の
	 * ”正”、”0”、”負”を判定し、
	 *  式と答えと結果を表示する

	 */
	public static void main(String[] args) {

		int a = 0;
		int b = 12;

		// 四則演算
		// ★足し算の結果を格納する
		int aTASUb = a + b;
		// 式を表示する（改行無）
		System.out.print("a + b = " + aTASUb);
		// if文を使って判定結果を表示する
		if (aTASUb == 0) { // ★足し算の結果が0かを判定
			System.out.println("　判定結果：0");
		} else if (aTASUb > 0) { // ★足し算の結果が0より大きいかを判定
			System.out.println("　判定結果：正");
		} else if (aTASUb < 0) { // ★足し算の結果が0より小さいかを判定
			System.out.println("　判定結果：負");
		}

		// ★引き算の結果を格納する
		int aHIKUb = a - b;
		// 式を表示する（改行無）
		System.out.print("a - b = " + aHIKUb);
		// if文を使って判定結果を表示する
		if (aHIKUb == 0) { // ★引き算の結果が0かを判定
			System.out.println("　判定結果：0");
		} else if (aHIKUb > 0) { // ★引き算の結果が0より大きいかを判定
			System.out.println("　判定結果：正");
		} else if (aHIKUb < 0) { // ★引き算の結果が0より小さいかを判定
			System.out.println("　判定結果：負");
		}


		// ★掛け算の結果を格納する
		int aKAKERUb = a * b;
		// 式を表示する（改行無）
		System.out.print("a * b = " + aKAKERUb);
		// if文を使って判定結果を表示する
		if (aKAKERUb == 0) { // ★引き算の結果が0かを判定
			System.out.println("　判定結果：0");
		} else if (aKAKERUb > 0) { // ★引き算の結果が0より大きいかを判定
			System.out.println("　判定結果：正");
		} else if (aKAKERUb < 0) { // ★引き算の結果が0より小さいかを判定
			System.out.println("　判定結果：負");
		}

		// ★割り算の結果を格納する
		int aWARIb = a / b;
		// 式を表示する（改行無）
		System.out.print("a / b = " + aWARIb);
		// if文を使って判定結果を表示する
		if (aWARIb == 0) { // ★引き算の結果が0かを判定
			System.out.println("　判定結果：0");
		} else if (aWARIb > 0) { // ★引き算の結果が0より大きいかを判定
			System.out.println("　判定結果：正");
		} else if (aWARIb < 0) { // ★引き算の結果が0より小さいかを判定
			System.out.println("　判定結果：負");
		}

	}

}
