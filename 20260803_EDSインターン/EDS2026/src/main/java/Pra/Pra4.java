package Pra;

public class Pra4 {

	/*
	 * 練習３に関してその数が
	 * ・3で割り切れるなら“Fizz“、
	 * ・5で割り切れるなら“Buzz“
	 * ・両方で割り切れるなら“FizzBuzz”
	 * が数字の下に表示されるようにする
	 */
	public static void main(String[] args) {

		// 1～20の数だけループする処理
		for (int i = 1; i <= 20; i++) {
			System.out.println("============"); // 見やすいように線を入れてます
			System.out.println(i); // カウントアップした変数を出力

			if () { // ★両方で割り切れるなら“FizzBuzz”
				System.out.println("FizzBuzz");
			} else if () {
				System.out.println("Fizz"); // ★3で割り切れるなら“Fizz“
			} else if () {
				System.out.println("Buzz"); // ★5で割り切れるなら“Buzz“
			}
			System.out.println("============"); // 見やすいように線を入れてます

		}

	}

}
