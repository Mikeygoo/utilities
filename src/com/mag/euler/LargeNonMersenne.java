package com.mag.euler;

/**
 *
 * @author michael
 */
public class LargeNonMersenne {
    public static void main(String[] args) {
        long twopow = 1;
        
        for (int i = 0; i < 7830457; i++)
            twopow = (twopow * 2) % 10000000000L;
        
        System.out.println((28433*twopow + 1) % 10000000000L);
    }
}
