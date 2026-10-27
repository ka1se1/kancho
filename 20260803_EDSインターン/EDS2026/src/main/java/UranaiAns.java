import java.util.Scanner;

public class UranaiAns {

	public static void main(String[] args) {
		// Uranaiクラスではint型の変数「num」をコンソールから入力します（Pra5Ans.java参照）
		Scanner scan = new Scanner(System.in);
		// 占う数字を入力する
		System.out.println("好きな数字は何ですか？");
		int num = scan.nextInt();

		// Uranaiクラス内でHanteiクラスのインスタンスを生成し、
		// numを与えることでunseiを受け取り、
		HanteiAns hantei = new HanteiAns(num);

		// それを占い結果としてコンソールに表示する式を作成します。
		System.out.println(hantei.getUnsei());

		scan.close();
	}
}
