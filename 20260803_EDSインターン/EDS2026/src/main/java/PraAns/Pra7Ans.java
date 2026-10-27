package PraAns;

public class Pra7Ans {

	/*
	 * if文を使って偶数と奇数、0を判別するプログラムを作成してください。
	 */
	public static void main(String[] args) {

		int kazu = 0; // ここを変える

		// 0じゃない、かつ偶数でも無ければ奇数とする
		if (kazu == 0) {
			System.out.println(kazu + "は偶数でも奇数でもありません。");
		} else if ((kazu % 2) == 0) {
			System.out.println(kazu + "は偶数です。");
		} else {
			System.out.println(kazu + "は奇数です。");
		}
	}
}
