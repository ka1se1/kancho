package Pra;

import java.util.Scanner;

public class Pra6 {

	/*
	 * 【練習６】練習５に対して支払い金額を入力し、お釣りを出力する処理を追加する
	 */
	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);

		// 一万円札を計算
		System.out.println("一万円札を何枚入れますか？");
		int manYen = scan.nextInt(); // このヒントを元に作成して下さい

		// 五千円札を計算
		System.out.println("五千円札を何枚入れますか？");
		// ★五千円札の枚数を受け付ける処理

		// 千円札を計算
		System.out.println("千円札を何枚入れますか？");
		// ★千円札の枚数を受け付ける処理

		// 五百円玉を計算
		System.out.println("五百円玉を何枚入れますか？");
		// ★五百円玉の枚数を受け付ける処理

		// 百円玉を計算
		System.out.println("百円玉を何枚入れますか？");
		// ★百円玉の枚数を受け付ける処理

		System.out.println("消費税は何%ですか？");
		// ★消費税の倍率を受け付ける処理

		System.out.println("税抜金額は : "); // ★税抜価格を表示する
		System.out.println("税込金額は : "); // ★税込価格を表示する

		int shiharai = scan.nextInt();

		// ★お釣りを計算する処理
		int oturi;

		// ★お釣りが-だった場合は不足とする
		if () {
			System.out.println("支払い金額が足りません！");
		} else {
			System.out.println("お釣りは : ");
		}

		scan.close();

	}

}
