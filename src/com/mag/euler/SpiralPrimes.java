package com.mag.euler;

import java.util.Iterator;

/**
 *
 * @author michael
 */
public class SpiralPrimes {
    public static void main(String[] args) {
        SpiralIterator sp = new SpiralIterator();
        int nums = 0, primes = 0;
        
        while (true) {
            nums++;
            
            int n = sp.next();
            
            if (isPrime(n)) {
                primes++;
            }
            
            if (nums > 10 && primes * 1.0 / nums < 0.1) {
                //One of the next 4 diagonals is going to be a perfect square... So why not print them all?
                System.out.println(Math.sqrt(n));
                System.out.println(Math.sqrt(sp.next()));
                System.out.println(Math.sqrt(sp.next()));
                System.out.println(Math.sqrt(sp.next()));
                return;
            }
        }
    }

    private static boolean isPrime(int n) {
        if (n < 10) {
            return n == 2 || n == 3 || n == 5 || n == 7;
        }
        
        int end = (int) Math.ceil(Math.sqrt(n));
        for (int i = 2; i <= end; i++)
            if (n % i == 0)
                return false;
        
        return true;
    }
    
    private static class SpiralIterator implements Iterator<Integer> {
        private int counter = 1, count = -1, delta = 2;

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Integer next() {
            count++;
            
            if (count == 4) {
                delta += 2;
                count = 0;
            }
            
            int save = counter;
            counter += delta;
            return save;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Operation not supported.");
        }
    }
}
