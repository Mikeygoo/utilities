package com.mag.arrayshow;

import java.awt.Color;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author michael
 */
class BreadthMergeSortingMethod extends SortingMethod {
    int[] temp;
    Deque<Run> runs = new LinkedList<Run>();

    public BreadthMergeSortingMethod() {
        super("Merge Sort (Breadth-First)");
    }

    @Override
    protected void sort(int[] a) {
        temp = new int[a.length];
        mergeSort0(a, 0, a.length);

        while (runs.size() > 1) {
            Run ra = runs.removeFirst(), rb = runs.removeFirst();

            if (ra.high != rb.low) {
                runs.addFirst(rb); //overshot it!
            } else {
                finalMerge0(a, ra.low, ra.high, rb.high);
                addRun(ra.low, rb.high);
            }
        }

        temp = null;
    }

    private void mergeSort0(int[] a, int low, int high) {
        if (high - low <= 1) {
            addRun(low, high);
            return;
        }

        if (high - low == 2) { //efficient!
            if (a[low] > a[low + 1])
                swap(low, low + 1);

            addRun(low, high);
            return;
        }

        int mid = (low + high) / 2;
        mergeSort0(a, low, mid);
        mergeSort0(a, mid, high);

        //finalMerge0(a, low, high);
    }

    private void finalMerge0(int[] a, int low, int mid, int high) {
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

    private void addRun(int low, int high) {
        runs.offer(new Run(low, high));
    }

    private class Run {
        int low, high;

        public Run(int low, int high) {
            this.low = low;
            this.high = high;
        }
    }
}
