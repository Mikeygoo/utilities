/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mag.sorting;

import java.util.Arrays;

public class QuickSort_Internet {
    private int[] numbers;
    private int number;

    public static void main(String[] args) {
        int[] a = {21, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        new QuickSort_Internet().sort(a);
        System.out.println(Arrays.toString(a));
    }

    public void sort(int[] values) {
        // check for empty or null array
        if (values == null || values.length == 0) {
            return;
        }

        this.numbers = values;
        number = values.length;
        quicksort(0, number - 1);
    }

    private void quicksort(int low, int high) {
        int i = low, j = high;
        // Get the pivot element from the middle of the list
        int pivot = numbers[pivot(numbers, low, high + 1)];

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller then the pivot
            // element then get the next element from the left list
            while (numbers[i] < pivot) {
                i++;
            }

            // If the current value from the right list is larger then the pivot
            // element then get the next element from the right list
            while (numbers[j] > pivot) {
                j--;
            }

            // If we have found a values in the left list which is larger then
            // the pivot element and if we have found a value in the right list
            // which is smaller then the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i <= j) {
                exchange(i, j);
                i++;
                j--;
            }
        }

        // Recursion
        printArray(numbers, low, j + 1);
        printArray(numbers, j + 1, i);
        printArray(numbers, i, high + 1);
        System.out.println();

        if (low < j)
            quicksort(low, j);

        if (i < high)
            quicksort(i, high);
    }

    private static int pivot(int[] a, int low, int high) {
        int i1 = low, i2 = high - 1, i3 = (low + high) / 2;
        int a1 = a[i1], a2 = a[i2], a3 = a[i3];

        int n = (a1 > a2) ? a1 : a2;
        int m = (a2 > a3) ? a2 : a3;

        if (m > n) {
            return (a1 > a2) ? i1 : i2;
        } else if (n > m) {
            return (a2 > a3) ? i2 : i3;
        } else if (n == m) {
            return (a1 > a3) ? i1 : i3;
        }

        return -1;
    }

    private void exchange(int i, int j) {
        int temp = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = temp;
    }

    private static void printQuick(String tabs, int[] a, int low, int i, int pivot, int high) {
        //System.out.println(tabs + Arrays.toString(a));
        System.out.print(tabs);
        printArray(a, low, i);
        System.out.print(" " + pivot + " ");
        printArray(a, i + 1, high);
        System.out.println();
    }

    private static void printArray(int[] a, int l, int h) {
        System.out.print("[");

        for (int i = l; i < h; i++) {
            if (i > l)
                System.out.print(", ");

            System.out.print(a[i]);
        }

        System.out.print("]");
    }
}
