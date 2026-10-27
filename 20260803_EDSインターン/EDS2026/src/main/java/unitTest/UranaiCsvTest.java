package unitTest;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uranaiCsv.UranaiCsv;

public class UranaiCsvTest {

	// CSVファイルのパスを指定
	String csvFilePath = "kimetu.csv";

	// 処理クラスのインスタンスを作成する
	UranaiCsv makeCsv = new UranaiCsv();

	BufferedReader br = null;

	List<String[]> dataList = new ArrayList<>();

	/**
	 * 最初に変数とかを準備する
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {

		br = new BufferedReader(new FileReader(csvFilePath));

		String line;
		// 1行を1つずつ配列に格納して配列のリストを作る
		while ((line = br.readLine()) != null) {
			String[] values = line.split(",");
			dataList.add(values);
		}
	}

	@Test
	public void testGetDataHairetsu1() {

		try {

			// 20行目のを取得するテスト
			int testGyou = 20;
			String[] ageResultHairetsu = makeCsv.getDataHairetsu(dataList, testGyou);

			/**
			 * 月,厄鏡・月映え,(やっきょう・つきばえ),紫色
			 * となっているかを確認
			 */
			// 月を確認
			if (ageResultHairetsu[0].equalsIgnoreCase("月")) {
				// OK
			} else {
				fail("月になってない：" + ageResultHairetsu[0]);
			}

			// 厄鏡・月映えを確認
			if (ageResultHairetsu[1].equalsIgnoreCase("厄鏡・月映え")) {
				// OK
			} else {
				fail("厄鏡・月映えになってない：" + ageResultHairetsu[1]);
			}

			// (やっきょう・つきばえ)を確認
			if (ageResultHairetsu[2].equalsIgnoreCase("(やっきょう・つきばえ)")) {
				// OK
			} else {
				fail("(やっきょう・つきばえ)になってない：" + ageResultHairetsu[2]);
			}

			// 紫色を確認
			if (ageResultHairetsu[3].equalsIgnoreCase("紫色")) {
				// OK
			} else {
				fail("紫色になってない：" + ageResultHairetsu[3]);
			}

		} catch (Exception e) {
			// エラーは失敗
			fail("エラー発生！！！！！");
		}

	}

	@Test
	public void testGetDataHairetsu2() {

		try {

			// 1行目のを取得してエラーにならない事を確認するテスト
			int testGyou = 1;
			String[] ageResultHairetsu = makeCsv.getDataHairetsu(dataList, testGyou);

		} catch (Exception e) {
			// エラーは失敗
			fail("エラー発生！！！！！");
		}

	}

	@Test
	public void testGetDataHairetsu3() {

		try {

			// 何行目を取得してもエラーにならない事を確認するテスト

		} catch (Exception e) {
			fail("エラー発生！！！！！");
		}

	}

}
