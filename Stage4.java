package encryptdecrypt;
import java.util.Scanner;

public class Stage4 {
    public static void main(String[] args) {
        String mode = "enc"; // command line option
        String data = "";    // command line option
        String valProp;
        String crypto = "";
        String ltr;
        int key = -1;        // command line option
        int idxArg;
        int idxOpt = 0;
        int numArgs = args.length;

        // loop through the args to find and set options
        for(String arg : args) {
            idxArg = idxOpt + 1;
            if(numArgs < idxArg+1 || args[idxArg] == null) {
                idxOpt++;
                continue;
            }
            valProp = args[idxArg];
            switch (arg) {
                case "-key" :
                    if (valProp.matches("[0-9]")) key = Integer.parseInt(valProp);
                    break;
                case "-data":
                    data = valProp.replaceAll("^\"|\"$", "");
                    break;
                case "-mode":
                    if (valProp.equals("enc") || valProp.equals("dec")) mode = valProp;
                    break;
                default:
                    break;
            }
            idxOpt++;
        }

        // prompt for missing options
        if (data.equals("") || key==-1) {
            Scanner scanner = new Scanner(System.in);
            if (data.equals("")) {
                System.out.println("Please specify data:");
                data = scanner.nextLine();
            }
            if (key == -1) {
                System.out.println("Please specify key:");
                key = scanner.nextInt();
            }
            scanner.close();
        }

        // loop through each char of data and encrypt or decrypt
        for (char ch: data.toCharArray()) {
            if (mode.equals("enc"))
                ltr = encryptChar(ch, key);
            else
                ltr = decryptChar(ch, key);

            crypto += ltr;
        }

        // output the encrypted / decrypted string
        System.out.println(crypto);
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
