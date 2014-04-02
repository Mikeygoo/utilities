package com.mag.arrayshow;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Thanks ethan!!
 * @author michael
 */
public class ArrayVisualizerPanel extends JPanel {
    public static boolean PARTIAL_BARS = true;
    public static boolean BARS = false;
    public static int MAXVAR = 100;
    public static int SLEEPTIME = 5;

    public static void main(String[] args) throws InterruptedException {
        final JFrame jf = new JFrame();
        final ArrayVisualizerPanel avp = new ArrayVisualizerPanel(100);
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

        while (true) {
//            avp.initializeRandom();
//            Thread.sleep(2000);
//            avp.sort(new CocktailSortingMethod());
//            Thread.sleep(2000);
//
//            avp.initializeRandom();
//            Thread.sleep(2000);
//            avp.sort(new InsertionSortingMethod());
//            Thread.sleep(2000);
//
//            avp.initializeRandom();
//            Thread.sleep(2000);
//            avp.sort(new SelectionSortingMethod());
//            Thread.sleep(2000);
//
//            avp.initializeRandom();
//            Thread.sleep(2000);
//            avp.sort(new StoogeSortingMethod());
//            Thread.sleep(2000);

            ArrayVisualizerPanel.SLEEPTIME *= 10;

            avp.initializeRandom();
            Thread.sleep(2000);
            avp.sort(new MostRadixSortingMethod());
            Thread.sleep(2000);

//            avp.initializeRandom();
//            Thread.sleep(2000);
//            avp.sort(new QuickSortingMethod());
//            Thread.sleep(2000);
//
//            ArrayVisualizerPanel.SLEEPTIME /= 2;
//
//            avp.initializeRandom();
//            Thread.sleep(2000);
//            avp.sort(new MergeSortingMethod());
//            Thread.sleep(2000);
//
//            avp.initializeRandom();
//            Thread.sleep(2000);
//            avp.sort(new HeapSortingMethod());
//            Thread.sleep(2000);
//
//            ArrayVisualizerPanel.SLEEPTIME /= 5;
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
                g.setColor(Color.DARK_GRAY);
                g.fillRect((int)(i * xscl), (int)(getHeight() - array[i] * yscl), Math.max((int)xscl, 2) - 1, ((int) Math.max(1, array[i] * yscl)));

                if (colarray[i] != null)
                    g.setColor(colarray[i]);
                else
                    g.setColor(Color.GREEN);

                g.fillRect((int)(i * xscl), (int)(getHeight() - array[i] * yscl), Math.max((int)xscl, 2) - 1, (getHeight() / MAXVAR));
            } else {
                if (colarray[i] != null)
                    g.setColor(colarray[i]);
                else
                    g.setColor(Color.GREEN);

                g.fillRect((int)(i * xscl), (int)(getHeight() - array[i] * yscl), Math.max((int)xscl, 2) - 1, BARS ? ((int) Math.max(1, array[i] * yscl)) : (getHeight() / MAXVAR));
            }
        }
    }

    public void sort(SortingMethod s) {
        System.out.println("started");
        long time = System.currentTimeMillis();
        this.title = s.getTitle();
        s.sort(array, this);
        this.title = "";
        System.out.printf("it took %d milliseconds\n", System.currentTimeMillis() - time);

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

    public void setColor(int index, Color color) {
        colarray[index] = color;
    }
}
