package encryptdecrypt;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Stage 5 of the Hyperskills Encryption-Decryption project
 *
 * After finishing the code I checked it to find out what problems needed
 * to be solved and was happy to discover that it worked on the first try.
 */
public class Stage5 {

    // command line options
    private static String mode = "enc";
    private static String data = "";
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
        String crypto = doCryptography();

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
                    if (valProp.matches("[0-9]")) key = Integer.parseInt(valProp);
                    break;
                case "-data":
                    data = valProp.replaceAll("^\"|\"$", "");
                    break;
                case "-mode":
                    if (valProp.equals("enc") || valProp.equals("dec")) mode = valProp;
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
        if (key == -1) {
            System.out.println("Crypto key is required.");
            System.exit(0);
        }
    }

    /**
     * loop through each char of data and encrypt or decrypt
     * @return String crypto
     */
    private static String doCryptography() {
        String crypto = "";
        String ltr;
        int cryptoVal;

        for (char ch: data.toCharArray()) {
            // (int)ch: num representing ch's place in unicode table
            if (mode.equals("enc")) {
                // advance unicode table position by key value
                cryptoVal = (int)ch + key;
            } else {
                // rewind unicode table position by key value
                cryptoVal = (int)ch - key;
            }
            // unicode char represented by cryptoVal
            ltr = String.valueOf((char)cryptoVal);

            crypto += ltr;
        }

        return crypto;
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
