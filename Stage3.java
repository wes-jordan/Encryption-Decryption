package encryptdecrypt;
import java.util.Scanner;

public class Stage3 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String cmd = scanner.nextLine();
        String msg = scanner.nextLine();
        int key = scanner.nextInt();

        String eMsg = "", ltr;

        for (char ch: msg.toCharArray()) {
            if (cmd.equals("enc"))
                ltr = encryptChar(ch, key);
            else
                ltr = decryptChar(ch, key);

            eMsg += ltr;
        }
        System.out.println(eMsg);
    }

    private static String encryptChar(char ch, int key) {
        // (int)ch: num representing ch's place in unicode table
        // advance unicode table position by key value
        int cryptoVal = (int)ch + key;
        // unicode char represented by cryptoVal
        return String.valueOf((char)cryptoVal);
    }

    private static String decryptChar(char ch, int key) {
        // (int)ch: num representing ch's place in unicode table
        // rewind unicode table position by key value
        int cryptoVal = (int)ch - key;
        // unicode char represented by cryptoVal
        return String.valueOf((char)cryptoVal);
    }
}
