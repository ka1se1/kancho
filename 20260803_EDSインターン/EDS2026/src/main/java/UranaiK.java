import java.util.Scanner;

public class UranaiK {

	public static void main(String[] args) {

		// UranaiKクラス内でHanteiクラスのインスタンスを生成し、
		// numを与えることでunseiを受け取り、
		// それを占い結果としてコンソールに表示する式を作成します。

		/**
		 * 時間が余ったらエラー処理入れてみてください。
		 * 数値以外のものが入力されたときなど
		 */

		// UranaiKクラスではコンソールから受け付けた
		//「年齢」「性別」「誕生日」をそれぞれ変数に定義します
		//（練習5参照）
		Scanner scan = new Scanner(System.in);

		// 年齢の入力を受け付ける
		System.out.println("年齢を入力してください。");
		int age = scan.nextInt();

		// 身長の入力を受け付ける
		System.out.println("身長を整数で入力してください（小数点切り上げ）");
		int height = scan.nextInt();

		// 誕生日の入力を受け付ける
		System.out.println("誕生日を入力してください（mmdd）");
		int birth = scan.nextInt();

		// UranaiKクラス内でHanteiKクラスのインスタンスを生成し、 
		// 「年齢」「身長」「誕生日」を与えることで
		// 「unseiMoney」「unseiJob」「unseiHealth」を受け取り、
		// それを占い結果としてコンソールに表示する式を作成します。

		// ★3つの値を引数にしてインスタンスを生成する処理
		// ★金運を出力する処理
		// ★仕事運を出力する処理
		// ★健康運を出力する処理

		scan.close();
	}
}
