package com.mag.arrayshow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author michael
 */
public class LeastRadixSortingMethod extends SortingMethod {
    private static final int RADIX = 2;
    private Queue[] registers = new Queue[RADIX];

    public LeastRadixSortingMethod() {
        super("Least-Significant-Digit Radix Sort");

        for (int i = 0; i < registers.length; i++) {
            registers[i] = new LinkedList<Integer>();
        }
    }

    @Override
    protected void sort(int[] a) {
        int largest = a[0];

        for (int n : a)
            if (largest < n)
                largest = n;

        int log = (int) Math.ceil(Math.log(largest) / Math.log(RADIX));

        for (int i = 0; i < log; i++) {
            for (int n : a)
                registers[getDigit(n, i)].offer(n);

            //printInfo(i);
            int index = 0;

            for (Queue register : registers) {
                while (!register.isEmpty()) {
                    set(index++, (Integer) register.poll());
                }
            }
        }
    }

    private void printInfo(int i) {
        System.out.println("for the " + Math.pow(RADIX, i) + "th/st power: ");

        for (int j = 0; j < RADIX; j++)
            System.out.println(j + ": " + registers[j]);
    }

    private int getDigit(int a, int pow) {
        return (int)(a / Math.pow(RADIX, pow)) % RADIX;
    }
}
