
import java.util.Arrays;
import java.util.Scanner;
import java.math.BigInteger;

/**
 *
 * @author michael
 */
public class LCD {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        while (true) {
            Scanner ln = new Scanner(s.nextLine());
            BigInteger[] ints = new BigInteger[100];
            Arrays.fill(ints, BigInteger.ONE);
            for (int i = 0; ln.hasNextInt() && i < 100; i++) {
                ints[i] = (ln.nextBigInteger());
            }
            System.out.printf("%d\n\n", LCD2(ints));
        }
    }
    
    public static final BigInteger LCD(BigInteger... ints) {
        BigInteger lcd = BigInteger.ONE;
        for (BigInteger n : ints) {
            if (lcd.mod(n).equals(BigInteger.ZERO))
                continue;
            for (BigInteger i = BigInteger.valueOf(2); i.compareTo(n) <= 0; i = i.add(BigInteger.ONE)) {
                if (n.mod(i).equals(BigInteger.ZERO)) {
                    BigInteger fact = BigInteger.ONE;
                    while (n.mod(i).equals(BigInteger.ZERO)) {
                        n = n.divide(i);
                        fact = fact.multiply(i);
                    }
                    if (!lcd.mod(fact).equals(BigInteger.ZERO)) {
                        while (lcd.mod(i).equals(BigInteger.ZERO)) {
                            lcd = lcd.divide(i);
                        }
                        lcd = lcd.multiply(fact);
                    }
                }
            }
        }
        return lcd;
    }
    
    public static BigInteger LCD2(BigInteger... ints) {
        BigInteger lcd = BigInteger.ONE;
        for (BigInteger n : ints) {
            lcd = lcd.divide(lcd.gcd(n)).multiply(n);
        }
        return lcd;
    }
}
