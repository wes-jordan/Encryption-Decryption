package encryptdecrypt;
import java.util.Scanner;

public class Stage2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String stdMsg = scanner.nextLine();
        int key = scanner.nextInt();

        String alpha = "abcdefghijklmnopqrstuvwxyz";
        String eMsg = "", ltr, sub;
        int idx;

        for (char ch: stdMsg.toCharArray()) {
            ltr = String.valueOf(ch);
            if (ltr.matches("[a-zA-Z]")) {
                idx = alpha.indexOf(ltr) + key;
                if (idx > 25) idx -= 26;
                sub = alpha.substring(idx,++idx);
                eMsg += sub;
            }
            else
                eMsg += ch;
        }
        System.out.println(eMsg);
    }
}