package com.mag.arrayshow;

import java.awt.Color;

/**
 *
 * @author michael
 */
class MergeSortingMethod extends SortingMethod {
    int[] temp;

    public MergeSortingMethod() {
        super("Merge Sort");
    }
    
    @Override
    protected void sort(int[] a) {
        temp = new int[a.length];
        mergeSort0(a, 0, a.length);
        temp = null;
    }

    private void mergeSort0(int[] a, int low, int high) {
        if (high - low <= 1)
            return;

        if (high - low == 2) { //efficient!
            if (a[low] > a[low + 1])
                swap(low, low + 1);

            return;
        }
        
        int mid = (low + high) / 2;
        mergeSort0(a, low, mid);
        mergeSort0(a, mid, high);
        
        finalMerge0(a, low, high);
    }

    private void finalMerge0(int[] a, int low, int high) {
        int mid = (low + high) / 2;
        int i = low, j = mid;
        int k = 0;
        while (i < mid && j < high) {
            if (a[i] < a[j]) {
                temp[k++] = a[i];
                i++;
            } else {
                temp[k++] = a[j];
                j++;
            }
        }
        
        while (i < mid) { //copy remaining i's
            temp[k++] = a[i];
            i++;
        }
        
        while (j < high) {
            temp[k++] = a[j];
            j++;
        }
        
        //copy it all back over
        for (k = 0; k < high - low; k++) {
            set(low + k, temp[k]);
        }
    }
}
