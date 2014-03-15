package com.mag.sorting;

import java.util.Arrays;

/**
 *
 * @author Kevin
 */
public class QuickSort {
    public static void main(String[] args) {
        int[] i = {9, 9, 9, 9, 9};
        quickSort0(i, 0, i.length, "");
        System.out.print(Arrays.toString(i));
    }

    public static int quickSort(int[] array) {
        if (array == null || array.length <= 1)
            return 1;

        quickSort0(array, 0, array.length - 1, "");
        return 0;
    }

    private static int quickSort0(int[] a, int low, int high, String tabs) { //the valid elements of the list are [low, high)
        if (high - low <= 1)
            return high - low;

        if (high - low == 2) { //efficient!
            if (a[low] > a[low + 1])
                swap(a, low, low + 1);

            return 1;
        }

        int n = high - low; //number of elements in the list.
        int p = pivot(a, low, high); //median of three; pivot's index.
        int pivot = a[p]; //p's value, the pivot value
        int i = low;
        int j = high - 2;
        swap(a, p, high - 1); //store the pivot at the end.

        while (i <= j) {
            while (a[i] < pivot && i < high - 2) { //don't run into the pivot we stored at index [high-1]
                i++;
            }

            while (a[j] > pivot && j > low) {
                j--;
            }

            if (i <= j) {
                swap(a, i, j);
                i++;
                j--;
            }
        }

        swap(a, i, high - 1); //put the pivot into its correct spot.

        printQuick(tabs, a, low, i, pivot, high);

        n += quickSort0(a, low, i, tabs + "    ");
        n += quickSort0(a, i + 1, high, tabs + "    ");

        return n;
    }

    private static void swap(int[] a, int i1, int i2) {
        int temp = a[i1];
        a[i1] = a[i2];
        a[i2] = temp;
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

    private static void printArray(int[] a, int l, int h) {
        System.out.print("[");

        for (int i = l; i < h; i++) {
            if (i > l)
                System.out.print(", ");

            System.out.print(a[i]);
        }

        System.out.print("]");
    }

    private static void printQuick(String tabs, int[] a, int low, int i, int pivot, int high) {
        //System.out.println(tabs + Arrays.toString(a));
        System.out.print(tabs);
        printArray(a, low, i);
        System.out.print(" " + pivot + " ");
        printArray(a, i + 1, high);
        System.out.println();
    }
}