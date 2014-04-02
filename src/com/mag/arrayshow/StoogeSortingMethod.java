package com.mag.arrayshow;

/**
 *
 * @author michael
 */
public class StoogeSortingMethod extends SortingMethod {
    public StoogeSortingMethod() {
        super("Stooge Sort");
    }

    @Override
    protected void sort(int[] a) {
        stoogeSort0(a, 0, a.length - 1);
    }

    private void stoogeSort0(int[] a, int low, int high) {
        if (a[low] > a[high])
            swap(high, low);
        if (high - low + 1 >= 3) {
            int t = (high - low + 1) / 3;
            stoogeSort0(a, low, high - t);
            stoogeSort0(a, low + t, high);
            stoogeSort0(a, low, high - t);
        }
    }
}
