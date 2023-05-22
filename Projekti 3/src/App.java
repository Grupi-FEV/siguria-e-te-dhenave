import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class App {
    public static void main(String[] args) {
        BinaryWords bw = new BinaryWords();
        RSA rsa = new RSA();
        Crypto c = new Crypto(rsa, bw);

        Path currentDir = Paths.get("").toAbsolutePath();
        Path plainTextPath;
        Path cypherPath;
        Path decypherPath;

        if (Files.exists(currentDir.resolve("output/plainText.txt"))){
            plainTextPath = currentDir.resolve("output/plainText.txt");
            cypherPath = currentDir.resolve("output/cypherText.txt");
            decypherPath = currentDir.resolve("output/decrypherText.txt");
        }
        else{
            plainTextPath = currentDir.resolve("src/output/plainText.txt");
            cypherPath = currentDir.resolve("src/output/cypherText.txt");
            decypherPath = currentDir.resolve("src/output/decrypherText.txt");
        }
        Charset encoding = StandardCharsets.UTF_8;
        
        String textString = "";
        try {
            textString = new String(Files.readAllBytes(plainTextPath), encoding);
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }

        String[] encrypted = c.encrypt(textString, rsa.getPublicKey());
        String encryptedText = joinArray(encrypted);
        System.out.println("Encrypted text with public key " + rsa.getPublicKey().toString() + ":\n" + encryptedText + "\n");
        try {
            Files.writeString(cypherPath, encryptedText, encoding);
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }

        String decrypted = c.decrypt(encrypted, rsa.getPrivateKey());
        System.out.println("Decrypted text with private key " + rsa.getPrivateKey().toString() + ":\n" + decrypted + "\n");
        try {
            Files.writeString(decypherPath, decrypted, encoding);
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    public static String joinArray(String[] arr){
        String r = "";
        for (int i = 0; i < arr.length; i++) {
            r += arr[i];
        }
        return r;
    }
}