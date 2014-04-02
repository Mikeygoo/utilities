package com.mag.arrayshow;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author michael
 */
public class BreadthMostRadixSortingMethod extends SortingMethod {
    private static final int RADIX = 5;
    private Queue[] registers = new Queue[RADIX];
    Deque<Run> runs = new LinkedList<Run>();

    public BreadthMostRadixSortingMethod() {
        super("Radix Sort (Breadth-First)");

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

        addRun(log - 1, 0, a.length);

        while (!runs.isEmpty()) {
            Run run = runs.removeFirst();
            radixSort0(a, run.pow, run.low, run.high);
        }
    }

    private void radixSort0(int[] a, int i, int low, int high) {
        if (i < 0)
            return;

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
            addRun(i - 1, low2, high2);
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

    private void addRun(int pow, int low, int high) {
        runs.addLast(new Run(pow, low, high));
    }

    private class Run {
        int pow, low, high;

        public Run(int pow, int low, int high) {
            this.pow = pow;
            this.low = low;
            this.high = high;
        }
    }
}
