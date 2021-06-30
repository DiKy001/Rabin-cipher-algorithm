import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class RabinCryptoSystem {
    private final int keyLength;
    private final BigInteger p;
    private final BigInteger q;
    private final BigInteger n;
    private final BigInteger b;
    private final Random random;
    private final BigInteger four = new BigInteger("4");

    public RabinCryptoSystem() {
        keyLength = 256;
        random = new Random();
        p = getPrimeNumMod4();
        q = getPrimeNumMod4();
        n = p.multiply(q);
        b = new BigInteger(n.bitLength() - 1, random);
    }

    public RabinCryptoSystem(int length) {
        keyLength = length / 2;
        random = new Random();
        p = getPrimeNumMod4();
        q = getPrimeNumMod4();
        n = p.multiply(q);
        b = new BigInteger(n.bitLength() - 1, random);
    }

    private BigInteger getPrimeNumMod4() {
        BigInteger num;
        BigInteger three = new BigInteger("3");
        do {
            num = BigInteger.probablePrime(keyLength, random);
        } while (!num.mod(four).equals(three));
        return num;
    }

    public String encrypt(String m, String nKey, String bKey) {
        BigInteger bb = new BigInteger(bKey, 16);
        BigInteger nn = new BigInteger(nKey, 16);
        BigInteger mNum = new BigInteger(m.getBytes(StandardCharsets.UTF_8));
        return mNum.multiply(mNum.add(bb)).mod(nn).toString(16);
    }

    public String encryptByPublicKeys(String m) {
        BigInteger mNum = new BigInteger(m.getBytes(StandardCharsets.UTF_8));
        return mNum.multiply(mNum.add(b)).mod(n).toString(16);
    }

    private void getEquationRoots(BigInteger[] roots) {
        BigInteger d0 = new BigInteger(String.valueOf(p));
        BigInteger d1 = new BigInteger(String.valueOf(q));
        BigInteger x0 = BigInteger.valueOf(1);
        BigInteger y0 = BigInteger.valueOf(0);
        roots[0] = BigInteger.valueOf(0);
        roots[1] = BigInteger.valueOf(1);
        while (d1.compareTo(BigInteger.valueOf(1)) > 0) {
            BigInteger q1 = d0.divide(d1);
            BigInteger d2 = d0.mod(d1);
            BigInteger x2 = x0.subtract(q1.multiply(roots[0]));
            BigInteger y2 = y0.subtract(q1.multiply(roots[1]));
            d0 = d1;
            d1 = d2;
            x0 = roots[0];
            roots[0] = x2;
            y0 = roots[1];
            roots[1] = y2;
        }
    }

    public String[] decrypt(String c) {
        BigInteger D = b.multiply(b).add((new BigInteger(c, 16)).multiply(four)).mod(n);
        BigInteger s = D.modPow(p.add(BigInteger.valueOf(1)).divide(four), p);
        BigInteger r = D.modPow(q.add(BigInteger.valueOf(1)).divide(four), q);
        BigInteger[] roots = new BigInteger[2];
        getEquationRoots(roots);
        BigInteger d1 = roots[0].multiply(p).multiply(r).add(roots[1].multiply(q).multiply(s));
        BigInteger d2 = d1.multiply(BigInteger.valueOf(-1));
        BigInteger d3 = roots[0].multiply(p).multiply(r).subtract(roots[1].multiply(q).multiply(s));
        BigInteger d4 = d3.multiply(BigInteger.valueOf(-1));
        String[] m = new String[4];
        m[0] = new String(d1.mod(n).subtract(b).divide(BigInteger.TWO).mod(n).toByteArray(), StandardCharsets.UTF_8);
        m[1] = new String(d2.mod(n).subtract(b).divide(BigInteger.TWO).mod(n).toByteArray(), StandardCharsets.UTF_8);
        m[2] = new String(d3.mod(n).subtract(b).divide(BigInteger.TWO).mod(n).toByteArray(), StandardCharsets.UTF_8);
        m[3] = new String(d4.mod(n).subtract(b).divide(BigInteger.TWO).mod(n).toByteArray(), StandardCharsets.UTF_8);
        return m;
    }

    public void printPublicKeys() {
        System.out.println("A pair of generated public keys:");
        System.out.println("n = " + n.toString(16));
        System.out.println("b = " + b.toString(16));
    }

}
