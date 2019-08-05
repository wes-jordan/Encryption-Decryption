package encryptdecrypt;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

/**
 * JetBrains Academy: Java Developer
 * Powered by: Hyperskills
 * https://hyperskill.org
 *
 * Project: Encryption-Decryption
 * @author Wes Jordan
 *
 * This is the final test of the 6 stage project.
 */
public class Main {

    // command line options
    private static String mode = "enc";
    private static String data = "";
    private static String alg = "";
    private static String in = "";
    private static String out = "";
    private static int key = -1;

    public static void main(String[] args) {

        // process the command line arguments
        processArgs(args);

        // prompt for missing command line options
        promptForArgs();

        // read input file for data if necessary
        readInputFile();

        // check for required options
        checkRqrdArgs();

        // encrypt or decrypt data
        CryptoLib cryptolib = new CryptoLib();
        String crypto = cryptolib.processCryptogram(mode, alg, data, key);

        // output the encrypted / decrypted string
        outputResult(crypto);
    }

    /**
     * loops through the args to find and set options
     * @param args command line arguments
     */
    private static void processArgs(String[] args) {
        String valProp;
        int idxArg;
        int idxOpt = 0;
        int numArgs = args.length;

        for(String arg : args) {
            idxArg = idxOpt + 1;
            if(numArgs < idxArg+1 || args[idxArg] == null) {
                idxOpt++;
                continue;
            }
            valProp = args[idxArg];
            switch (arg) {
                case "-key" :
                    if (valProp.matches("^[0-9]+$")) key = Integer.parseInt(valProp);
                    break;
                case "-data":
                    data = valProp.replaceAll("^\"|\"$", "");
                    break;
                case "-mode":
                    valProp = valProp.toUpperCase(Locale.ENGLISH);
                    if (valProp.equals("ENC") || valProp.equals("DEC")) mode = valProp;
                    break;
                case "-alg":
                    valProp = valProp.toUpperCase(Locale.ENGLISH);
                    if (valProp.equals("SHIFT") || valProp.equals("UNICODE")) alg = valProp;
                    break;
                case "-in":
                    in = valProp;
                    break;
                case "-out":
                    out = valProp;
                    break;
                default:
                    break;
            }
            idxOpt++;
        }
    }

    /**
     * prompts the user for missing command line options
     */
    private static void promptForArgs() {
        if (data.equals("") && in.equals("")) {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Please specify data or input file:");
                String input = scanner.nextLine();
                // determine if the input is a filepath or data
                if (input.matches("[a-zA-Z0-9]/")) {
                    in = input;
                } else {
                    data = input;
                }
            } catch (Exception e) {
                System.out.printf("An exception occurred: %s", e.getMessage());
                System.exit(0);
            }
        }
        if (key==-1) {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Please specify key:");
                key = scanner.nextInt();
            } catch (Exception e) {
                System.out.printf("An exception occurred: %s", e.getMessage());
                System.exit(0);
            }
        }
    }

    /**
     * reads the input file to populate the data var
     */
    private static void readInputFile() {
        if (in.equals("")) return;

        File file = new File(in);
        try (Scanner scanner = new Scanner(file)) {
            data = scanner.nextLine().trim();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            System.exit(0);
        }
    }

    /**
     * checks that data and key have both been set
     */
    private static void checkRqrdArgs() {
        if (data.equals("")) {
            System.out.println("Crypto data not found.");
            System.exit(0);
        }
        if (alg.equals("")) {
            System.out.println("Algorithm not found.");
            System.exit(0);
        }
        if (key == -1) {
            System.out.println("Crypto key is required.");
            System.exit(0);
        }
    }

    /**
     * output the result to standard output or a file
     * @param crypto data post cryptography
     */
    private static void outputResult(String crypto) {
        // output to standard output
        if (out.equals("")) {
            System.out.println(crypto);
            return;
        }

        // output to a file
        File file = new File(out);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(crypto);
        } catch (IOException e) {
            System.out.printf("An exception occurred: %s", e.getMessage());
        }
    }
}

/**
 * The follow classes use the Factory Method to
 * implement two different encryption algorithms.
 */

/* Creator */
abstract class Crypto {
    String crypto = "";
    String data;
    int key;

    Crypto(String data, int key) {
        this.data = data;
        this.key = key;
    }

    String getResult() {
        return crypto;
    }

    abstract void encrypt();
    abstract void decrypt();
}

/* Concrete Creators */
class CryptoShift extends Crypto {
    private String alpha = "abcdefghijklmnopqrstuvwxyz";
    private String ltr;
    int idx;

    CryptoShift(String data, int key) {
        super(data, key);
    }

    @Override
    public void encrypt() {
        for (char ch: data.toCharArray()) {
            ltr = String.valueOf(ch);
            if (ltr.matches("[a-zA-Z]")) {
                idx = alpha.indexOf(ltr) + key;
                if (idx > 25) idx -= 26;
                crypto += alpha.substring(idx, ++idx);
            }
            else
                crypto += ch;
        }
    }

    @Override
    public void decrypt() {
        for (char ch: data.toCharArray()) {
            ltr = String.valueOf(ch);
            if (ltr.matches("[a-zA-Z]")) {
                idx = alpha.indexOf(ltr) - key;
                if (idx < 0) idx += 26;
                crypto += alpha.substring(idx, ++idx);
            }
            else
                crypto += ch;
        }
    }
}

class CryptoUnicode extends Crypto {
    private String ltr;
    private int cryptoVal;

    CryptoUnicode(String data, int key) {
        super(data, key);
    }

    @Override
    public void encrypt() {
        for (char ch: data.toCharArray()) {
            // advance unicode table position by key value
            cryptoVal = (int)ch + key;

            // unicode char represented by cryptoVal
            ltr = String.valueOf((char)cryptoVal);

            crypto += ltr;
        }
    }

    @Override
    public void decrypt() {
        for (char ch: data.toCharArray()) {
            // rewind unicode table position by key value
            cryptoVal = (int)ch - key;

            // unicode char represented by cryptoVal
            ltr = String.valueOf((char)cryptoVal);

            crypto += ltr;
        }
    }
}

/* Product */
abstract class CryptoFactory {

    abstract String processCryptogram(String mode, String alg, String data, int key);

    Crypto doCryptography(String mode, String alg, String data, int key) {
        Crypto crypto = null;
        switch (alg.toUpperCase(Locale.ENGLISH)) {
            case "SHIFT":
                crypto = new CryptoShift(data, key);
                break;
            case "UNICODE":
                crypto = new CryptoUnicode(data, key);
                break;
            default:
                System.out.println("Invalid algorithm specified: "+alg);
                System.exit(0);
        }

        mode = mode.toUpperCase(Locale.ENGLISH);
        if (mode.equals("ENC")) {
            crypto.encrypt();
        } else if (mode.equals("DEC")) {
            crypto.decrypt();
        } else {
            System.out.println("Invalid mode specified: "+mode);
            System.exit(0);
        }

        return crypto;
    }
}

/* Concrete Product */
class CryptoLib extends CryptoFactory {

    @Override
    String processCryptogram(String mode, String alg, String data, int key) {
        Crypto crypto = doCryptography(mode, alg, data, key);
        return crypto.getResult();
    }
}