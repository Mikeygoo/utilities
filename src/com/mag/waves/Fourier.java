package com.mag.waves;

import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author michael
 */
public class Fourier extends JPanel {
    public static final int WIDTH = 500, HEIGHT = 500;
    public static final int COLUMNS = 20;
    
    public static void main(String[] args) throws InterruptedException {
        Fourier f = new Fourier();
        f.setSize(WIDTH, HEIGHT);
        
        JFrame jf = new JFrame("Fourier");
        jf.setSize(WIDTH, HEIGHT);
        jf.add(f);
        jf.setVisible(true);
        
        while (true) {
            Thread.sleep(5);
            f.logic();
            jf.repaint();
        }
    }
    
    private Rectangle[] cols = new Rectangle[COLUMNS];
    private int i = 0;

    public Fourier() {
        
    }

    @Override
    public void paint(Graphics g) {
        
    }

    private void logic() {
        if (allRectanglesGone())
            nextIteration();
        
        for (int i = 0; i < COLUMNS; i++) {
            //lets animate one rectangle.
            //TODO: calculate new position using d(t)
            //TODO: check for out of bounds
        }
    }

    private boolean allRectanglesGone() {
        for (Rectangle c : cols)
            if (c != null)
                return false;
        return true;
    }

    private void nextIteration() {
        //TODO: regenerate rectangles
        //          - Calculate fourier height for x @ midpoint of rect.
        //          - Fourier height is rectangle riemann sum part @ that
    }
    
    private class Rectangle {
        
    }
}
