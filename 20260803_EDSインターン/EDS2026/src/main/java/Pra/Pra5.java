package Pra;

import java.util.Scanner;

public class Pra5 {

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

		scan.close();

	}

}
