package PraAns;

import java.util.Scanner;

public class Pra6Ans {

	/*
	 * 【練習６】練習５に対して支払い金額を入力し、お釣りを出力する処理を追加する
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

		System.out.println("支払い金額を入力してください。");

		// 支払い金額を受け付ける
		int shiharai = scan.nextInt();

		// お釣りを計算する
		int oturi = shiharai - (int) sumZei;

		// お釣りが0だった場合は不足とする
		if (oturi < 0) {
			System.out.println("支払い金額が足りません！");
		} else {
			System.out.println("お釣りは : " + oturi);
		}

		scan.close();

	}

}
