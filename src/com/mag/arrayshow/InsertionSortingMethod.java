package com.mag.arrayshow;

/**
 *
 * @author michael
 */
public class InsertionSortingMethod extends SortingMethod {
    public InsertionSortingMethod() {
        super("Insertion Sort");
    }

    @Override
    protected void sort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int j = i;
            while (j > 0 && a[j-1] > a[j]) {
                swap(j, j - 1);
                j--;
            }
        }
    }
}
