import java.math.BigInteger;

public class SimpleExample {
    public static void main(String[] args) {
        BigInteger p = new BigInteger("11");
        BigInteger q = new BigInteger("23");
        BigInteger n = p.multiply(q);
        RSA rsa = new RSA(p, q);

        BigInteger e = new BigInteger("31");
        BigInteger d = e.modInverse(new BigInteger("220"));
        Pair<BigInteger, BigInteger> publicKey = new Pair<BigInteger,BigInteger>(e, n);
        Pair<BigInteger, BigInteger> privateKey = new Pair<BigInteger,BigInteger>(d, n);
        
        BigInteger m = new BigInteger("10");
        BigInteger ct = rsa.encrypt(m, publicKey);
        BigInteger pt = rsa.decrypt(ct, privateKey);
        
        System.out.println("Mesazhi m="+m+" i enkriptuar me celesin publik "+publicKey.toString()+" eshte: "+ct);
        System.out.println("Mesazhi c="+ct+" i dekriptuar me celesin privat "+privateKey.toString()+" eshte: "+pt);
    }
}
