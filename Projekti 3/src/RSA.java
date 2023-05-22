import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA {
    private BigInteger p;
    private BigInteger q;
    private BigInteger publicKey;
    private BigInteger privateKey;
    private BigInteger n;
    private final static SecureRandom random = new SecureRandom();

    public RSA(){
        setP(generateRandomPrime(9));
        setQ(generateRandomPrime(9));
        n = p.multiply(q);
        generateKeys();
    }

    public RSA(BigInteger p, BigInteger q){
        setP(p);
        setQ(q);
        n = p.multiply(q);
        generateKeys();
    }
    
    public void setP(BigInteger newP){
        if(!newP.isProbablePrime(10000))
            p = newP.nextProbablePrime();
        else
            p = new BigInteger(newP.toByteArray());
    }

    public BigInteger getP(){
        return(new BigInteger(p.toByteArray()));
    }

    public void setQ(BigInteger newQ){
        if(!newQ.isProbablePrime(10000))
            q = newQ.nextProbablePrime();
        else
            q = new BigInteger(newQ.toByteArray());
    }

    public BigInteger getQ(){
        return(new BigInteger(q.toByteArray()));
    }

    public BigInteger generateRandomPrime(int bitLength) {
        return BigInteger.probablePrime(bitLength, random);
    }

    private void generateKeys(){
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = generatePublicKey(phi);
        BigInteger d = e.modInverse(phi);
        publicKey = e;
        privateKey = d;
        n = p.multiply(q);
    }

    private BigInteger generatePublicKey(BigInteger n){
        BigInteger r;
        do {
            r = new BigInteger(n.bitLength(), random);
        } while (r.compareTo(BigInteger.ONE) <= 0 || r.compareTo(n) >= 0 || !r.gcd(n).equals(BigInteger.ONE));
        return r;
    }

    public void generateNewKeys(){
        generateKeys();
    }

    public Pair<BigInteger, BigInteger> getPublicKey(){
        return new Pair<BigInteger,BigInteger>(publicKey, n);
    }
    
    public Pair<BigInteger, BigInteger> getPrivateKey(){
        return new Pair<BigInteger,BigInteger>(privateKey, n);
    }

    public BigInteger encrypt(BigInteger m){
        BigInteger c = m.modPow(publicKey, n);
        return c;
    }

    public BigInteger decrypt(BigInteger c){
        BigInteger m = c.modPow(privateKey, n);
        return m;
    }

    // Method to use when you want to encrypt with a certain key.
    public BigInteger encrypt(BigInteger m, Pair<BigInteger,BigInteger> publicKey){
        BigInteger c = m.modPow(publicKey.first, publicKey.second);
        return c;
    }

    // Method to use when you want to decrypt with a certain key.
    public BigInteger decrypt(BigInteger c, Pair<BigInteger,BigInteger> privateKey){
        BigInteger m = c.modPow(privateKey.first, privateKey.second);
        return m;
    }
}