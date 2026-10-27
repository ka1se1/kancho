package PraAns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Add2Ans {
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(System.in));
		int sum = 0;

		while (sum <= 100)
			sum += Integer.parseInt(br.readLine());

		System.out.println("合計は " + sum);
	}
}
