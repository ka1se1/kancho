package uranaiCsv;

import java.util.Scanner;

public class UranaiCsvMain {

	public static void main(String[] args) {

		// CSVファイルのパスを指定
		String csvFilePath = "kimetu.csv";

		/**
		 * 最初に表示するアスキーアート
		 */
		System.out.println("    *****    ");
		System.out.println("  *       *  ");
		System.out.println(" *  o   o  * ");
		System.out.println("*     ^     *");
		System.out.println("*    '-'    *");
		System.out.println(" *         * ");
		System.out.println("  *       *  ");
		System.out.println("    *****    ");

		// コンソールからの入力を受け付けるScannerクラス
		try (Scanner scan = new Scanner(System.in)) {
			// 年齢の入力を受け付ける
			System.out.println("年齢を入力してください。");
			int age = scan.nextInt();

			// 身長の入力を受け付ける
			System.out.println("身長を整数で入力してください（小数点切り上げ）");
			int height = scan.nextInt();

			// 誕生日の入力を受け付ける
			System.out.println("誕生日を入力してください（mmdd）");
			int birth = scan.nextInt();

			// 処理クラスのインスタンスを作成する
			UranaiCsv makeCsv = new UranaiCsv();
			// 実際の処理クラスに情報を渡して呼び出す
			makeCsv.readCsv(csvFilePath, age, height, birth);
		}

	}

}
