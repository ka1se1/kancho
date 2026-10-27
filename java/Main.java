public class Main {
    // これはコメントです
    public static void main(String[] args) {
        String messageString = "じゃんけんぽん！ (0:グー 1:チョキ 2:パー) > ";
        int n = 0;
        while(n == 0){
            System.out.print(messageString);
            int cpu = new java.util.Random().nextInt(3);
            int hands = new java.util.Scanner(System.in).nextInt();
            if (hands < 0 || hands > 2) {
                System.out.println("無効な選択です。もう一度入力してください。");
                continue;
            }
            if (cpu == hands) {
                System.out.println("あいこです。");
            } else if ((hands == 0 && cpu == 1) || (hands == 1 && cpu == 2) || (hands == 2 && cpu == 0)) {
                System.out.println("あなたの勝ちです！");
                n = 1;
            } else {
                System.out.println("あなたの負けです。");
                n = 1;
            }
            System.out.println("CPUの手は " + (cpu == 0 ? "グー" : cpu == 1 ? "チョキ" : "パー") + " でした。");
            System.out.print("もう一度プレイしますか？ (0:はい 1:いいえ) > ");
            n = new java.util.Scanner(System.in).nextInt();
        }
    }
}

//final int a = 3; にすれば、aの値を変更できなくなります。