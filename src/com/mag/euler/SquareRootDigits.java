package com.mag.euler;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Problem 80
 * @author michael
 */
public class SquareRootDigits {
    public static void main(String[] args) {
        int sum = 0;
        
        for (int i = 1; i < 100; i++) {
            if (isPerfectSquare(i))
                continue;
            
            sum += digitalSum(sqrtBigDecimal(new BigDecimal(i)));
        }
        
        System.out.println(sum);
    }

    private static final BigDecimal TWO = new BigDecimal(2);
    private static final MathContext mc = new MathContext(105);
    
    private static BigDecimal sqrtBigDecimal(BigDecimal d) {
        BigDecimal x = new BigDecimal(20);
        BigDecimal x_old = null;
        
        for (int i = 0; i < 50; i++) {
            //System.out.println("Iteration #"+i+": " + x);
            x_old = x;
            
            x = x_old.subtract((x_old.pow(2, mc).subtract(d, mc)).divide(x_old.multiply(TWO, mc), mc));
        }
        
        return x;
    }

    private static int digitalSum(BigDecimal n) {
        String s = n.toString();
        s = s.replaceAll("\\.", "");
        s = s.substring(0, 100);
        
        int sum = 0;
        for (char c : s.toCharArray())
            sum += c - '0';
        
        return sum;
    }

    private static boolean isPerfectSquare(int i) {
        return i == ((int) Math.sqrt(i)) * ((int) Math.sqrt(i));
    }
}
