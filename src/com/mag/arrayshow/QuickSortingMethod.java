package com.mag.arrayshow;

import java.awt.Color;

/**
 *
 * @author michael
 */
public class QuickSortingMethod extends SortingMethod {
    public QuickSortingMethod() {
        super("Quick Sort");
    }
    
    public void sort(int[] array) {
        if (array == null || array.length <= 1)
            return;

        quickSort0(array, 0, array.length);
    }

    private void quickSort0(int[] a, int low, int high) { //the valid elements of the list are [low, high]
        if (high - low <= 1)
            return;

        if (high - low == 2) { //efficient!
            if (a[low] > a[low + 1])
                swap(low, low + 1);

            return;
        }
        
//        if (high - low < 20) {
//            for (int i = low; i < high - 1; i++) {
//                focus(i, Color.BLUE);
//                int lowest = i;
//                for (int j = i + 1; j < high; j++) {
//                    //focus(j, Color.yellow);
//                    if (a[lowest] > a[j])
//                        lowest = j;
//                    //focus(j, null);
//                }
//                swap(i, lowest);
//                focus(i, null);
//            }
//        }

        int n = high - low; //number of elements in the list.
        int p = pivot(a, low, high); //median of three; pivot's index.
        int pivot = a[p]; //p's value, the pivot value
        int i = low;
        int j = high - 2;
        swap(p, high - 1); //store the pivot at the end.

        while (i <= j) {
            while (a[i] < pivot && i < high - 2) { //don't run into the pivot we stored at index [high-1]
                i++;
            }

            while (a[j] > pivot && j > low) {
                j--;
            }

            if (i <= j) {
                swap(i, j);
                i++;
                j--;
            }
        }

        swap(i, high - 1); //put the pivot into its correct spot.
        focus(i, Color.BLUE);

        quickSort0(a, low, i);
        quickSort0(a, i + 1, high);
        focus(i, null);
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
}
