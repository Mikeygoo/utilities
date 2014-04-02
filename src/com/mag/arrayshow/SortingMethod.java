package com.mag.arrayshow;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author michael
 */
public abstract class SortingMethod {
    private ArrayVisualizerPanel focus;
    private int[] arr;
    private String title;

    public SortingMethod(String title) {
        this.title = title;
    }

    public void sort(int[] a, ArrayVisualizerPanel j) {
        focus = j;
        arr = a;
        sort(a);
    };

    protected abstract void sort(int[] a);

    protected void swap(int i, int j) {
        //System.out.println("swap "+i+", "+j);
        focus(i, Color.RED);
        focus(j, Color.RED);
        focus.repaint();

        try {
            Thread.sleep(ArrayVisualizerPanel.SLEEPTIME);
        } catch (InterruptedException ex) {
            Logger.getLogger(SortingMethod.class.getName()).log(Level.SEVERE, null, ex);
        }

        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        focus(i, null);
        focus(j, null);
    }

    protected void set(int index, int val) {
        arr[index] = val;
        focus(index, Color.RED);

        try {
            Thread.sleep(ArrayVisualizerPanel.SLEEPTIME);
        } catch (InterruptedException ex) {
            Logger.getLogger(SortingMethod.class.getName()).log(Level.SEVERE, null, ex);
        }

        focus(index, null);
    }

    protected void halt() {
        try {
            Thread.sleep(ArrayVisualizerPanel.SLEEPTIME);
        } catch (InterruptedException ex) {
            Logger.getLogger(SortingMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void focus(int index, Color c) {
        focus.setColor(index, c);
        focus.repaint();
    }

    public String getTitle() {
        return title;
    }
}
