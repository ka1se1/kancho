package PraAns;

import java.util.Scanner;

public class Pra5Ans {

	/*
	 * 【練習５】コンソールで各種紙幣（二千円札は除く）と500円玉、100円玉の入力を受け付け
	 *    ①その総額を表示
	 *   消費税の入力を受け付け
	 *    ②総額に対しての税込み価格を表示
	 */
	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);

		// 一万円札を計算
		System.out.println("一万円札を何枚入れますか？");
		int manYen = scan.nextInt();

		// 五千円札を計算
		System.out.println("五千円札を何枚入れますか？");
		int gosenYen = scan.nextInt();

		// 千円札を計算
		System.out.println("千円札を何枚入れますか？");
		int senYen = scan.nextInt();

		// 五百円玉を計算
		System.out.println("五百円玉を何枚入れますか？");
		int gohyakuYen = scan.nextInt();

		// 百円玉を計算
		System.out.println("百円玉を何枚入れますか？");
		int hyakuYen = scan.nextInt();

		System.out.println("消費税は何%ですか？");
		int syouhiZei = scan.nextInt();

		int sum = (manYen * 10000) + (gosenYen * 5000) + (senYen * 1000) + (gohyakuYen * 500) + (hyakuYen * 100);
		double sumZei = sum * ((double) (100 + syouhiZei) / 100);
		System.out.println("税抜金額は : " + sum);
		System.out.println("税込金額は : " + (int) sumZei); // intにして小数点を丸めた

		scan.close();

	}

}
