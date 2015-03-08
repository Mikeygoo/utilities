package com.mag.img;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author michael
 */
public class ColorFitter extends JFrame {
    public static void main(String[] args) throws IOException {
        //args = new String[] {"/home/michael/Pictures/Webcam/2013-12-29-112856.jpg", //to
        //                     "/home/michael/Pictures/Webcam/2013-12-29-112903.jpg"};//from
        args = new String[] {"/home/michael/Programming/carA.png",
                             "/home/michael/Programming/grad.jpg"};
        
        if (args.length != 2) {
            System.out.println("Please provide two image files!");
            System.exit(-1);
        }
        
        final BufferedImage imageA = ImageIO.read(new File(args[0]));
        final BufferedImage imageB = ImageIO.read(new File(args[1]));
        
        if (imageA.getWidth() * imageA.getHeight() != 
                imageB.getWidth() * imageB.getHeight()) {
            System.out.printf("Warning: This program requires that both images "
                      + "have equivalent areas; however, %s has an area of %d, "
                      + "and %s has an area of %d.", 
                    args[0], imageA.getWidth() * imageA.getHeight(),
                    args[1], imageB.getWidth() * imageB.getHeight());
        }
        
        new Thread() {
            @Override
            public void run() {
                new ColorFitter(imageA, imageB, true).start();
            }
        }.start();
        
        //new Thread() {
        //    @Override
        //    public void run() {
        //        new ColorFitter(imageA, imageB, false).start();
        //    }
        //}.start();
    }
    
    private BufferedImage drawingSurface;
    private PImage originalA, originalB;
    private HashMap<Pixel, Pixel> bestFitImage = new HashMap<Pixel, Pixel>();
    private int aw, ah, bw, bh;
    private boolean new_bias;
    
    public ColorFitter(BufferedImage imageA, BufferedImage imageB, boolean new_bias) {
        originalA = createPixelLists(imageA);
        originalB = createPixelLists(imageB);
        aw = originalA.imageBuffer.length;
        ah = originalA.imageBuffer[0].length;
        bw = originalB.imageBuffer.length;
        bh = originalB.imageBuffer[0].length;
        drawingSurface = new BufferedImage(aw, ah, BufferedImage.TYPE_INT_RGB);
        this.new_bias = new_bias;
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Saving");
                try {
                    exportImage(aw, ah, BufferedImage.TYPE_INT_RGB);
                } catch (IOException ex) {
                    Logger.getLogger(ColorFitter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    public void start() {
        for (int i = 0; i < aw * ah; i++) {
            Pixel pixA = originalA.imageBuffer[i % aw][i / aw % ah];
            Pixel pixB = originalB.imageBuffer[i % bw][i / bw % bh];
            bestFitImage.put(pixA, pixB);
        }
        
        setVisible(true);
        setSize(aw, ah);
        repaint();
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ColorFitter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (int i = 0; i < aw * ah; i++) {
            swapAlways();
            repaint();
        }
        
        while (true) {
            int temp = 0;
            for (int i = 0; i < 1000; i++)
                if (swap()) temp++;
            if (temp == 0)
                ;//break;
            if (false)
                break;
            repaint();
        }
        
        System.out.println("Finished.");
        
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ColorFitter.class.getName()).log(Level.SEVERE, null, ex);
            }
            repaint();
        }
    }
    
    private void swapAlways() {
        int p1 = (int) (aw * ah * Math.random());
        int p2 = (int) (aw * ah * Math.random());
        
        Pixel a1 = originalA.imageBuffer[p1 % aw][p1 / aw];
        Pixel a2 = originalA.imageBuffer[p2 % aw][p2 / aw];
        Pixel b1 = bestFitImage.get(a1);
        Pixel b2 = bestFitImage.get(a2);
        
        bestFitImage.put(a1, b2);
        bestFitImage.put(a2, b1);
    }
    
    private boolean swap() {
        int p1 = (int) (aw * ah * Math.random());
        int p2 = (int) (aw * ah * Math.random());
        
        Pixel a1 = originalA.imageBuffer[p1 % aw][p1 / aw];
        Pixel a2 = originalA.imageBuffer[p2 % aw][p2 / aw];
        Pixel b1 = bestFitImage.get(a1);
        Pixel b2 = bestFitImage.get(a2);
        
        if (compare(a1, b1) + compare(a2, b2) > compare(a1, b2) + compare(a2, b1)) {
            bestFitImage.put(a1, b2);
            bestFitImage.put(a2, b1);
            return true;
        }
        
        return false;
    }

    @Override
    public void paint(Graphics g) {
        int[] temp = {0, 0, 0};
        WritableRaster raster = drawingSurface.getRaster();
        for (Entry<Pixel, Pixel> e : bestFitImage.entrySet()) {
            Pixel a = e.getKey(), b = e.getValue();
            temp[0] = b.r;
            temp[1] = b.g;
            temp[2] = b.b;
            raster.setPixel(a.x, a.y, temp);
        }
        g.drawImage(drawingSurface, 0, 0, rootPane);
        g.setColor(Color.black);
        g.drawString(new_bias ? "luma" : "none", 30, 30);
    }
    
    private double compare(Pixel m, Pixel n) {
        if (new_bias) {
            //CIE L*a*b* distance
            double deltaL = m.L - n.L;
            double deltaA = m.A - n.A;
            double deltaB = m.B - n.B;
            double c1 = sqrt(pow(m.A) + pow(m.B));
            double c2 = sqrt(pow(n.A) + pow(n.B));
            double deltaCab = c1 - c2;
            double deltaHab = sqrt(pow(deltaA) + pow(deltaB) - pow(deltaCab));
            double sL = 1;
            double sC = 1 + 0.045 * c1;
            double sH = 1 + 0.015 * c2;
            
            return sqrt(pow(deltaL / sL) + pow(deltaCab / sC) + pow(deltaHab / sH));
        } else {
            // 3D RGB distance
            return (0.299*pow(m.r - n.r) + 0.587*pow(m.g - n.g) + 0.114*pow(m.b - n.b));
            //return sqrt(pow(m.r - n.r) + pow(m.g - n.g) + pow(m.b - n.b));
        }
    }
    
    private double pow(double i) {
        return i * i;
    }

    private void exportImage(int w, int h, int type) throws IOException {
        BufferedImage output = new BufferedImage(w, h, type);
        
        WritableRaster outputRaster = output.getRaster();
        int[] rgb = {0, 0, 0};
        
        for (Map.Entry<Pixel, Pixel> e : bestFitImage.entrySet()) {
            Pixel a = e.getKey();
            Pixel b = e.getValue();
            rgb[0] = b.r;
            rgb[1] = b.g;
            rgb[2] = b.b;
            outputRaster.setPixel(a.x, a.y, rgb);
        }
        
        System.out.println("Okay, saving.");
        ImageIO.write(output, "png", new File("output.png"));
        System.out.println("Saved!");
    }

    private static ArrayList<Pixel> createArrayListFrom(ArrayList<Pixel> list, Comparator<Pixel> sort) {
        ArrayList<Pixel> set = new ArrayList<>();
        set.addAll(list);
        Collections.sort(list, sort);
        return set;
    }

    private static PImage createPixelLists(BufferedImage img) {
        PImage pimage = new PImage();
        WritableRaster raster = img.getRaster();
        
        pimage.imageBuffer = new Pixel[img.getWidth()][img.getHeight()];
        
        int[] temp = {0, 0, 0};
        float[] tempF = {0, 0, 0};
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                Pixel p = new Pixel();
                p.x = x;
                p.y = y;
                raster.getPixel(x, y, temp);
                p.r = temp[0];
                p.g = temp[1];
                p.b = temp[2];
                new Color(p.r, p.g, p.b).getColorComponents(
                                ColorSpace.getInstance(ColorSpace.CS_CIEXYZ), 
                                tempF);
                
                //"normalized" reference point.
                double Xn =  /*96.42;*/ 95.047;
                double Yn = 100.000;
                double Zn = /*82.49;*/ 108.883;
                
                double X = tempF[0];
                double Y = tempF[1];
                double Z = tempF[2];
                
                p.L = 166*f(Y/Yn) - 16;
                p.A = 500*(f(X/Xn) - f(Y/Yn));
                p.B = 200*(f(Y/Yn) - f(Z/Zn));
                pimage.imageBuffer[x][y] = p;
            }
        }
        
        return pimage;
    }
    
    private static double f(double x) {
        if (x > Math.pow(6.0/29, 3))
            return Math.pow(x, 1.0/3);
        return 29.0*29.0/6.0/6.0/3.0*x+4.0/29.0; // (1/3)(29/6)^2*x+4/29
    }
    
    private static class Pixel {
        public int x, y;
        public int r, g, b;
        public double L, A, B;
    }
    
    private static class PImage {
        Pixel[][] imageBuffer;
    }
}