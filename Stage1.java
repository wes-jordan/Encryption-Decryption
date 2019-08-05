package encryptdecrypt;

public class Stage1 {
    public static void main(String[] args) {
        String alpha1 = "abcdefghijklmnopqrstuvwxyz";
        String alpha2 = "zyxwvutsrqponmlkjihgfedcba";
        String dMsg = "we found a treasure!", eMsg = "", ltr1, ltr2;
        int idx;
        for (char ch: dMsg.toCharArray()) {
            ltr1 = String.valueOf(ch);
            if (ltr1.matches("[a-zA-Z0-9]")) {
                idx = alpha1.indexOf(ltr1);
                ltr2 = alpha2.substring(idx,++idx);
                eMsg += ltr2;
            }
            else
                eMsg += ch;
        }
        System.out.println(eMsg);
    }
}