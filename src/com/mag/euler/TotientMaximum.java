package com.mag.euler;

/**
 *
 * @author michael
 */
public class TotientMaximum {
    public static int[] some_primes = new int[1000];
    
    public static void main(String[] args) {
        //first, let's populate the list of primes.
        int a = 0, b = 2;
        
        while (a < 1000) {
            if (isPrime(b))
                some_primes[a++] = b;
            b++;
        }
        
        // -------------------------------------- //
        
        double noverphi = 0;
        int n = 0;
        
        for (int i = 2; i < 1000000; i++) {
            //System.out.println(i);
            double phi = phi(i);
            
            if (noverphi < i / phi) {
                System.out.println("new n @ " + i);
                n = i;
                noverphi = i / phi;
            }
        }
        
        System.out.println("final n: " + n);
    }
    
    public static int phi(int num) {
        if (num == 1)
            return 1;
        
        if (isPrime(num))
            return num-1;
        
        if (num % 2 == 0) { //using 2 identity
            num /= 2;
            return (num % 2 == 0 ? 2 : 1) * phi(num);
        }
        
        //this is the identity: phi(m * n) = phi(m) * phi(n) * gcd(m, n) / phi(gcd(m, n)).
        
        int m = findMeADivisor(num), n = num / m;
        int g = gcd(m, n);
        
        return phi(m) * phi(n) * g / phi(g);
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

    private static int gcd(int u, int v) {
        // simple cases (termination)
        if (u == v)
            return u;

        if (u == 0)
            return v;

        if (v == 0)
            return u;

        // look for factors of 2
        if ((~u & 1) == 1) // u is even
        {
            if ((v & 1) == 1) // v is odd
                return gcd(u >> 1, v);
            else // both u and v are even
                return gcd(u >> 1, v >> 1) << 1;
        }

        if ((~v & 1) == 1) // u is odd, v is even
            return gcd(u, v >> 1);

        // reduce larger argument
        if (u > v)
            return gcd((u - v) >> 1, v);

        return gcd((v - u) >> 1, u);
    }

    private static int findMeADivisor(int num) {
        for (int prime : some_primes)
            if (num % prime == 0)
                return prime;
        
        //otherwise...
        System.out.println("I've had to bruteforce it...");
        
        for (int i = some_primes[some_primes.length - 1] + 1; i < num; i++)
            if (num % i == 0)
                return i;
        
        throw new RuntimeException("frick it @ " + num);
    }
}
