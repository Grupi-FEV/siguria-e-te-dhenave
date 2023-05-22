import java.math.BigInteger;

public class Crypto {
    RSA rsa;
    BinaryWords bw;

    Crypto(RSA rsa, BinaryWords bw){
        this.rsa = rsa;
        this.bw = bw;
    }

    public String[] encrypt(String plainText, Pair<BigInteger, BigInteger> publicKey){
        BigInteger n = publicKey.second;
        int log2n = n.bitLength() - 1;
        int blockLength = (log2n/8)*8;
        String binaryText = bw.toBinary(plainText);
        String[] encryptedText = new String[binaryText.length()%blockLength == 0? binaryText.length()/blockLength : binaryText.length()/blockLength+1];
        for (int i = 0; i < encryptedText.length; i++) {
            if((i+1)*blockLength <= binaryText.length())
                encryptedText[i] = binaryText.substring(i*blockLength, (i+1)*blockLength);
            else{
                encryptedText[i] = binaryText.substring(i*blockLength);
            }
            BigInteger decimalBlock = new BigInteger(encryptedText[i],2);
            BigInteger encryptedBlock = rsa.encrypt(decimalBlock, publicKey);
            String encryptedBits = encryptedBlock.toString(2);
            while (encryptedBits.length() % blockLength != 0) {
                encryptedBits = "0" + encryptedBits;
            }
            encryptedText[i] = bw.toString(encryptedBits);
        }
        return encryptedText;
    };

    public String decrypt(String[] cypherText, Pair<BigInteger, BigInteger> secretKey){
        String decryptedText = "";
        BigInteger n = secretKey.second;
        int log2n = n.bitLength() - 1;
        int blockLength = (log2n/8)*8;

        for (int i = 0; i < cypherText.length; i++) {
            String binaryText = bw.toBinary(cypherText[i]);
            BigInteger decimalBlock = new BigInteger(binaryText,2);
            BigInteger decryptedBlock = rsa.decrypt(decimalBlock, secretKey);
            String decryptedBits = decryptedBlock.toString(2);
            while (decryptedBits.length() % blockLength != 0) {
                decryptedBits = "0" + decryptedBits;
            }
            decryptedText += decryptedBits;
        }
        return bw.toString(decryptedText);
    };
}