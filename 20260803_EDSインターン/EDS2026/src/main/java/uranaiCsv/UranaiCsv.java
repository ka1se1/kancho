package uranaiCsv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UranaiCsv {
	public void readCsv(String filePath, int age, int height, int birth) {

		// 引数のfilePathからcsvファイルを読み込む
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

			/**
			 * csvの情報を変数に格納する処理
			 */
			String line;
			List<String[]> dataList = new ArrayList<>();

			// 1行を1つずつ配列に格納して配列のリストを作る
			// ----以下、参考----
			// 1個目のリスト：呼吸,型,読み方,刀の色
			// 2個目のリスト：日,円舞,(えんぶ),黒色
			// 3個目のリスト：日,碧羅の天,(へきらのてん),黒色
			// 4個目のリスト：日,烈日紅鏡,(れつじつこうきょう),黒色
			// こんな感じで配列ごとにリストにしてます(「,」で分けてます、A,BならAとBみたいな）
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				dataList.add(values);
			}

			/**
			 * 入力値からcsvの値を取得する
			 */
			// age編
			// 配列リストから年齢と同じ行数に格納されている値を取得する
			String[] ageResultHairetsu = getDataHairetsu(dataList, age);
			String ageResult = getData(ageResultHairetsu, 0);

			// height編
			// ★何か処理を追加してみてください

			// birth編
			// ★何か処理を追加してみてください

			/*************************************************
			 * 結果出力
			 *************************************************/
			System.out.println("◆◇◆◇◆◇◆◇◆◇◆◇◆◇◆◇◆◇◆◇◆◇◆◇◆◇◆◇");
			System.out.println("あなたの目指すべき型は" + ageResult + "の型です");
			// ★何か出力を追加してみてください（ラッキーカラーでもなんでも）
			// ★何か出力を追加してみてください（ラッキーアイテムでもなんでも）
			System.out.println("◆◇◆◇◆◇◆◇◆◇◆◇◆◇◆◇◆◇◆◇◆◇◆◇◆◇◆◇");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ▼▼▼▼▼各処理メソッド▼▼▼▼▼
	 */
	// 引数1から引数2と同じ行数に格納されている値を返却する
	public String[] getDataHairetsu(List<String[]> dataList, int num) {
		// 返却用の変数に引数1から引数2と同じ行数に格納されている値を格納する
		String[] resultHairetsu = dataList.get(num);

		return resultHairetsu;

	}

	// 引数1の文字列配列から引数2の場所に格納されている値を返却する
	public String getData(String[] resultHairetsu, int i) {
		// 返却用の変数に引数1の配列から引数2の場所に格納されている値を格納する
		String result = resultHairetsu[i];

		return result;
	}

}