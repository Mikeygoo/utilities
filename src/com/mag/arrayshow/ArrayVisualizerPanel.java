package com.mag.arrayshow;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Thanks ethan!!
 * @author michael
 */
public class ArrayVisualizerPanel extends JPanel {
    public static boolean PARTIAL_BARS = true;
    public static boolean BARS = false;
    public static int MAXVAR = 250;
    public static int SLEEPTIME = 1;

    public static void main(String[] args) throws InterruptedException {
        final JFrame jf = new JFrame();
        final ArrayVisualizerPanel avp = new ArrayVisualizerPanel(250);
        
        jf.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                avp.setSize(jf.getWidth(), jf.getHeight() - 20);
            }
        });
        
        jf.add(avp);
        jf.setSize(750, 750);
        jf.setVisible(true);
        avp.setVisible(true);
        
        avp.initialize();
        avp.sort(new TimeSortingMethod());
        
        while (true) {
            avp.initialize();
            avp.sort(new BubbleSortingMethod());
            Thread.sleep(2000);
            
            avp.initialize();
            avp.sort(new CocktailSortingMethod());
            Thread.sleep(2000);

            avp.initialize();
            avp.sort(new InsertionSortingMethod());
            Thread.sleep(2000);

            avp.initialize();
            avp.sort(new StoogeSortingMethod());
            Thread.sleep(2000);

            ArrayVisualizerPanel.SLEEPTIME *= 10;

            avp.initialize();
            avp.sort(new SelectionSortingMethod());
            Thread.sleep(2000);
            
            ArrayVisualizerPanel.SLEEPTIME /= 1;

            avp.initialize();
            avp.sort(new BreadthMostRadixSortingMethod());
            Thread.sleep(2000);
            
            avp.initialize();
            avp.sort(new LeastRadixSortingMethod());
            Thread.sleep(2000);

            avp.initialize();
            avp.sort(new QuickSortingMethod());
            Thread.sleep(2000);

            ArrayVisualizerPanel.SLEEPTIME /= 2;

            avp.initialize();
            avp.sort(new MergeSortingMethod());
            Thread.sleep(2000);

            avp.initialize();
            avp.sort(new HeapSortingMethod());
            Thread.sleep(2000);

            ArrayVisualizerPanel.SLEEPTIME /= 5;
        }
    }

    private int[] array;
    private Color[] colarray;
    private String title = "";

    public ArrayVisualizerPanel(int[] array) {
        this.array = array;
        colarray = new Color[array.length];
    }

    public ArrayVisualizerPanel(int arraysize) {
        this.array = new int[arraysize];
        colarray = new Color[arraysize];
    }

    @Override
    public void paint(Graphics g) {
        double xscl = (double) getWidth() / array.length;
        double yscl = (double) getHeight() / MAXVAR;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.drawString(title, 0, 10);
        
        for (int i = 0; i < array.length; i++) {
            
            if (PARTIAL_BARS) {
                g.setColor(BAR_COLOR);
                g.fillRect((int)(i * xscl), (int)(getHeight() - array[i] * yscl), Math.max((int)xscl, 2) - 1, ((int) Math.max(1, array[i] * yscl)));

                if (colarray[i] != null)
                    g.setColor(colarray[i]);
                else
                    g.setColor(ELEMENT_COLOR);

                g.fillRect((int)(i * xscl), (int)(getHeight() - array[i] * yscl), Math.max((int)xscl, 2) - 1, (int) yscl);
            } else {
                if (colarray[i] != null)
                    g.setColor(colarray[i]);
                else
                    g.setColor(ELEMENT_COLOR);

                g.fillRect((int)(i * xscl), (int)(getHeight() - array[i] * yscl), Math.max((int)xscl, 2) - 1, BARS ? ((int) Math.max(1, array[i] * yscl)) : (int) yscl);
            }
        }
    }
    public static final Color BAR_COLOR = Color.DARK_GRAY;
    public static final Color ELEMENT_COLOR = Color.GREEN;

    public void sort(SortingMethod s) {
        System.out.println("started");
        long time = System.currentTimeMillis();
        this.title = s.getTitle();
        s.sort(array, this);
        this.title = "";
        System.out.printf("it took %d milliseconds\n", System.currentTimeMillis() - time);
    }
    
    private void initialize() {
        initializeRandom();
        //initializeReversed();
        //initializeMostlySortedReversed();
    }

    private void initializeRandom() {
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }

        for (int i = 0; i < array.length; i++) {
            int j = (int)(Math.random() * array.length);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        repaint();
    }

    private void initializeBad() {
        for (int i = 1; i < array.length; i++) {
            array[i] = i;
        }

        array[0] = array.length;
    }
    
    private void initializeMostlySorted() {
        initializeRandom();
        quickSortMostly0(array, 0, array.length);
    }
    
    private void initializeMostlySortedReversed() {
        initializeRandom();
        quickSortMostly0(array, 0, array.length);
        for (int i = 0; i < array.length / 2; i++)
            swap(array, i, array.length - i - 1);
    }
    
    private void quickSortMostly0(int[] a, int low, int high) { //the valid elements of the list are [low, high]
        if (high - low <= a.length / 10)
            return;

        int n = high - low; //number of elements in the list.
        int p = low + 1; //median of three; pivot's index.
        int pivot = a[p]; //p's value, the pivot value
        int i = low;
        int j = high - 2;
        initializeReversed();
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

        quickSortMostly0(a, low, i);
        quickSortMostly0(a, i + 1, high);
    }

    private void swap(int[] a, int i, int j) {    
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    private void initializeReversed() {
        for (int i = 0; i < array.length; i++) {
            array[i] = array.length - i;
        }
    }

    public void setColor(int index, Color color) {
        colarray[index] = color;
    }
}
