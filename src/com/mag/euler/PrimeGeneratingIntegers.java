package com.mag.euler;

/**
 *
 * @author michael
 */
public class PrimeGeneratingIntegers {
    public static void main(String[] args) {
        long sum = 0;
        
        outer: for (int i = 1; i <= 100_000_000; i++) {
            if (!isPrime(i))
                continue;
            
            int n = i - 1;
            int end = (int) Math.ceil(Math.sqrt(n + 1));
            
            for (int j = 2; j < end; j++)
                if (n % j == 0 && !isPrime(j + n / j))
                    continue outer;
            
            sum += n;
            System.out.println("> " + n);
        }
        
        System.out.println(">>> " + sum);
    }

    private static boolean isPrime(int n) {
        if (n < 10) {
            return n == 2 || n == 3 || n == 5 || n == 7;
        }
        
        if (n % 2 == 0 || n % 3 == 0)
            return false;
        
        int end = (int) Math.ceil(Math.sqrt(n));
        for (int i = 5; i <= end; i += 6)
            if (n % i == 0 || n % (i + 2) == 0)
                return false;
        
        return true;
    }
}
