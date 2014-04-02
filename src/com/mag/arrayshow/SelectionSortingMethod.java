package com.mag.arrayshow;

import java.awt.Color;

/**
 *
 * @author michael
 */
public class SelectionSortingMethod extends SortingMethod {
    public SelectionSortingMethod() {
        super("Selection Sort");
    }

    @Override
    protected void sort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            focus(i, Color.BLUE);
            int lowest = i;

            for (int j = i + 1; j < a.length; j++) {
                //focus(j, Color.yellow);
                if (a[lowest] > a[j])
                    lowest = j;

                //focus(j, null);
                //halt();
            }

            swap(i, lowest);
            focus(i, null);
        }
    }
}
