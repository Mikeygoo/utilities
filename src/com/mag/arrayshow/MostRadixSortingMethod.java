package com.mag.arrayshow;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author michael
 */
public class MostRadixSortingMethod extends SortingMethod {
    private static final int RADIX = 5;

    public MostRadixSortingMethod() {
        super("Most-Significant-Digit Radix Sort");
    }

    @Override
    protected void sort(int[] a) {
        int largest = a[0];

        for (int n : a)
            if (largest < n)
                largest = n;

        int log = (int) Math.ceil(Math.log(largest) / Math.log(RADIX));

        radixSort0(a, log - 1, 0, a.length);
    }

    private void radixSort0(int[] a, int i, int low, int high) {
        if (i < 0)
            return;
        
        Queue[] registers = new Queue[RADIX];
        
        for (int n = 0; n < registers.length; n++) {
            registers[n] = new LinkedList<Integer>();
        }

        for (int j = low; j < high; j++)
            registers[getDigit(a[j], i)].offer(a[j]);

        //printInfo(i);
        int index = low;

        for (Queue register : registers) {
            int low2 = index;

            while (!register.isEmpty()) {
                set(index++, (Integer) register.poll());
            }

            int high2 = index;
            radixSort0(a, i - 1, low2, high2);
        }
    }

    private int getDigit(int a, int pow) {
        return (int)(a / Math.pow(RADIX, pow)) % RADIX;
    }
}
