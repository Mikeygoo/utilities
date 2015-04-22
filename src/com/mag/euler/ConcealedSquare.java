package com.mag.euler;

/**
 *
 * @author michael
 */
public class ConcealedSquare {
    public static void main(String[] args) {
        for (long i = (long) 1e8; i < 2e8; i+=1) {
            String s = (i * i) + "";
            if (s.matches("1.2.3.4.5.6.7.8.9")) {
                System.out.println(10*i);
                return;
            }
        }
    }
}
