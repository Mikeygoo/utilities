package com.mag.arrayshow;

/**
 *
 * @author michael
 */
public class CocktailSortingMethod extends SortingMethod {
    public CocktailSortingMethod() {
        super("Cocktail Sort");
    }

    @Override
    protected void sort(int[] a) {
        while (true) {
            boolean swapped = false;

            for (int i = 1; i < a.length; i++) {
                if (a[i - 1] > a[i]) {
                    swap(i - 1, i);
                    swapped = true;
                }
            }
            
            for (int i = a.length - 2; i >= 0; i--) {
                if (a[i + 1] < a[i]) {
                    swap(i + 1, i);
                    swapped = true;
                }
            }

            if (!swapped)
                break;
        }
    }
}
