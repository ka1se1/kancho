package PraAns;

public class Pra10Ans {

	/*
	 * 配列に{20,30,70,10,50,40}を設定し、
	 * この中から最小値と最大値を出力するプログラムを作成してください。
	 */
	public static void main(String[] args) {
		// 配列{20,30,70,10,50,40}を設定
		int[] hairetu = { 20, 30, 70, 10, 50, 40 };
		// 最大値を入れる変数
		int maxSuti = 0;
		// 最小値を入れる変数
		int miniSuti = 100;

		// 配列の数だけ数値を取り出してループ処理を行う
		for (int suti : hairetu) {
			// 最大値より取り出した数値が大きければ置き換える
			if (maxSuti < suti) {
				maxSuti = suti;
			}
			// 最小値より取り出した数値が小さければ置き換える
			if (miniSuti > suti) {
				miniSuti = suti;
			}
		}
		System.out.println("最大値は：" + maxSuti);
		System.out.println("最小値は：" + miniSuti);
	}
}
