package com.mag.euler;

import java.util.HashSet;

/**
 *
 * @author michael
 */
public class SquareDigitChains {
    private static HashSet<Integer> one = new HashSet<>(), eightyNine = new HashSet<>();
    
    public static void main(String[] args) {
        int count = 0;
        
        for (int i = 2; i < 10_000_000; i++)
            if (sumsToEigtyNine(i)) count++;
        
        System.out.println(count);
    }

    private static boolean sumsToEigtyNine(int i) {
        if (i == 1) return false;
        if (i == 89) return true;
        
        if (i < 1000) {
            if (one.contains(i)) return false;
            if (eightyNine.contains(i)) return true;
        }
        
        int sum = 0;
        for (char c : (i + "").toCharArray()) {
            int n = (c - '0');
            
            sum += n * n;
        }
        
        boolean subsum = sumsToEigtyNine(sum);
        
        if (i < 1000) {
            (subsum ? eightyNine : one).add(i);
        }
        
        return subsum;
    }
}
