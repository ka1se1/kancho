package PraAns;

public class Pra1Ans {

	public static void main(String[] args) {
		// 5と12を整数型変数に格納する
		int a = 5;
		int b = 12;

		// 四則演算
		System.out.println("足し算");
		System.out.println(a + b);
		System.out.println("引き算");
		System.out.println(a - b);
		System.out.println("掛け算");
		System.out.println(a * b);
		System.out.println("割り算");
		System.out.println(a / b); // 0になってしまう
		System.out.println((double) a / b); // 小数点も表示する

	}

}
