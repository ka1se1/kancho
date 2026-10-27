package whileTest;

import java.io.File;

public class WhileTest {
	public static void main(String[] args) {

		// 停止で利用するファイル名を定義する
		File file = new File("stop.txt");

		// エラー発生で利用するファイル名を定義する
		File eFile = new File("error.txt");

		int count = 1;

		// ファイルが置かれるまで処理を繰り返す
		while (true) {

			try {
				System.out.println(count + "回目の処理");
				// 1秒止める
				Thread.sleep(1000);

				count++;

				// ファイルの存在を確認する
				if (file.exists()) {
					// 存在すれば処理終了
					System.out.println("ファイルが存在します");
					System.out.println("処理を終了します");
					break;
				} else {
					// 存在しない場合は処理継続
					System.out.println("ファイルが存在しません");
				}

				// エラー用のファイルの存在を確認する
				if (eFile.exists()) {
					// ファイルが存在した場合、エラーを投げる
					throw new IllegalArgumentException("エラーファイルが存在しました");
				} else {
					// 何もしない
				}

			} catch (Exception e) {
				// エラーになっても処理を継続する
				System.out.println("エラーが発生しました");
				// エラー内容だけ出力
				e.printStackTrace();
			}
		}
	}
}