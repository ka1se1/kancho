package PraAns;

public class Pra8Ans {

	/*
	 * for文を使って12星座を出力するプログラムを作成してください。
	 */
	public static void main(String[] args) {
		// 12星座を配列にする
		String[] seizaList = { "牡羊座", "双子座", "獅子座", "天秤座", "射手座",
				"水瓶座", "牡牛座", "蟹座", "乙女座", "蠍座", "山羊座", "魚座" };

		// ArrayListに一つずつ入れてもいい
		//		ArrayList<String> seizaList = new ArrayList<String>();
		//		seizaList.add("牡羊座");
		//		seizaList.add("双子座");
		//		seizaList.add("獅子座");
		//           ～以下略～

		// for文で一つずつ配列から文字列を取り出す
		for (String seiza : seizaList) {
			System.out.println(seiza);
		}
	}
}
