package PraAns;

public class Pra9Ans {

	/*
	 * 1から100まで足した合計を出力するプログラムを作成してください。
	 */
	public static void main(String[] args) {
		// 数値を足していく用の変数
		int sum = 0;
		// 1～100まで100回ループさせてどんどん足していく
		for (int i = 1; i <= 100; i++) {
			sum = sum + i;
		}
		System.out.println("1から100までの合計値は：" + sum);
	}
}
