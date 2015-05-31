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
import java.util.Random;
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
        args = new String[] {"/home/michael/Programming/MonaLisa.png",
                             "/home/michael/Programming/AmericanGothic.png"};
        
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
                new ColorFitter(imageA, imageB, false, true).start();
            }
        }.start();
        
        new Thread() {
            @Override
            public void run() {
                new ColorFitter(imageA, imageB, false, false).start();
            }
        }.start();
        
        //new Thread() {
        //    @Override
        //    public void run() {
        //        new ColorFitter(imageA, imageB, false).start();
        //    }
        //}.start();
    }
    
    private static final double delta = 0.00001;
    
    private Random r = new Random(0);
    private BufferedImage drawingSurface;
    private PImage originalA, originalB;
    private HashMap<Pixel, Pixel> bestFitImage = new HashMap<Pixel, Pixel>();
    private int aw, ah, bw, bh;
    private boolean new_bias, neighbors;
    
    public ColorFitter(BufferedImage imageA, BufferedImage imageB, boolean new_bias, boolean neighbors) {
        originalA = createPixelLists(imageA);
        originalB = createPixelLists(imageB);
        aw = originalA.imageBuffer.length;
        ah = originalA.imageBuffer[0].length;
        bw = originalB.imageBuffer.length;
        bh = originalB.imageBuffer[0].length;
        drawingSurface = new BufferedImage(aw, ah, BufferedImage.TYPE_INT_RGB);
        this.new_bias = new_bias;
        this.neighbors = neighbors;
        
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
        
        for (int i = 0; i < aw * ah*10; i++) {
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
        int p1 = (int) (aw * ah * r.nextDouble());
        int p2 = (int) (aw * ah * r.nextDouble());
        
        Pixel a1 = originalA.imageBuffer[p1 % aw][p1 / aw];
        Pixel a2 = originalA.imageBuffer[p2 % aw][p2 / aw];
        Pixel b1 = bestFitImage.get(a1);
        Pixel b2 = bestFitImage.get(a2);
        
        bestFitImage.put(a1, b2);
        bestFitImage.put(a2, b1);
    }
    
    int swaps;
    private boolean swap() {
        swaps++;
        if (swaps%1000000==0) System.out.println("swaps:"+swaps);
        if (swaps%100000000==0) {
            try {
                exportImage(aw, ah, BufferedImage.TYPE_INT_RGB);
            } catch (IOException ex) {
                Logger.getLogger(ColorFitter.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Okay, bye!");
            while(true);
        }
        int p1 = (int) (aw * ah * r.nextDouble());
        int p2 = (int) (aw * ah * r.nextDouble());
        
        Pixel a1 = originalA.imageBuffer[p1 % aw][p1 / aw];
        Pixel a2 = originalA.imageBuffer[p2 % aw][p2 / aw];
        Pixel b1 = bestFitImage.get(a1);
        Pixel b2 = bestFitImage.get(a2);
        
        double pixelOnly = compare(a1, b1) + compare(a2, b2) - compare(a1, b2) - compare(a2, b1);
        
        if (pixelOnly > delta) {
            bestFitImage.put(a1, b2);
            bestFitImage.put(a2, b1);
            return true;
        } else if (pixelOnly > 0 && neighbors) {
            //System.out.println("calculating neighborhood");
            int ra1 = 0, ga1 = 0, ba1 = 0;
            int ra2 = 0, ga2 = 0, ba2 = 0;
            int rb1 = 0, gb1 = 0, bb1 = 0;
            int rb2 = 0, gb2 = 0, bb2 = 0;
            int count = 0;
            for (int i = -1; i <= +1; i++) {
                if ((p1%aw) + i < 0 || (p1%aw) + i >= aw || (p2 % aw) + i < 0 || (p2 % aw) + i >= aw)
                    continue;
                for (int j = -1; j <= 1; j++) {
                    if ((p1/aw) + j < 0 || (p1/aw) + j >= ah || (p2/aw) + j < 0 || (p2/aw) + j >= ah) 
                        continue;
                    count++;
                    Pixel a10 = originalA.imageBuffer[p1 % aw+i][p1 / aw+j];
                    Pixel a20 = originalA.imageBuffer[p2 % aw+i][p2 / aw+j];
                    Pixel b10 = bestFitImage.get(a10);
                    Pixel b20 = bestFitImage.get(a20);
                    ra1 += a10.r;
                    ra2 += a20.r;
                    rb1 += b10.r;
                    rb2 += b20.r;
                    
                    ga1 += a10.g;
                    ga2 += a20.g;
                    gb1 += b10.g;
                    gb2 += b20.g;
                    
                    ba1 += a10.b;
                    ba2 += a20.b;
                    bb1 += b10.b;
                    bb2 += b20.b;
                }
                
                ra1 /= count;
                ra2 /= count;
                rb1 /= count;
                rb2 /= count;
                ga1 /= count;
                ga2 /= count;
                gb1 /= count;
                gb2 /= count;
                ba1 /= count;
                ba2 /= count;
                bb1 /= count;
                bb2 /= count;
            }
            Pixel a1a = new Pixel(ra1, ga1, ba1);
            Pixel a2a = new Pixel(ra2, ga2, ba2);
            Pixel b1a = new Pixel(rb1, gb1, bb1);
            Pixel b2a = new Pixel(rb2, gb2, bb2);
            
            double familyPixels = compare(a1a, b1a) + compare(a2a, b2a) - compare(a1a, b2a) - compare(a2a, b1a);
            if (familyPixels > 0) {
                //System.out.println("swapped thanks to neighbor pixels.");
                bestFitImage.put(a1, b2);
                bestFitImage.put(a2, b1);
                return true;
            }
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
        g.drawString((new_bias ? "luma" : "none") + " " + (neighbors ? "neighbors" : "single"), 30, 30);
    }
    
    private double compare(Pixel m, Pixel n) {
        double weight = 0;
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
            
            weight = sqrt(pow(deltaL / sL) + pow(deltaCab / sC) + pow(deltaHab / sH));
        } else {
            // 3D RGB distance
            weight = (0.299*pow(m.r - n.r) + 0.587*pow(m.g - n.g) + 0.114*pow(m.b - n.b));
            //return sqrt(pow(m.r - n.r) + pow(m.g - n.g) + pow(m.b - n.b));
        }
        return weight;
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
        ImageIO.write(output, "png", new File("output"+(new_bias?"_luma":"_noluma")+(neighbors?"_neighbors":"_single")+".png"));
        System.out.println("Saved!");
    }

    private PImage createPixelLists(BufferedImage img) {
        PImage pimage = new PImage();
        WritableRaster raster = img.getRaster();
        
        pimage.imageBuffer = new Pixel[img.getWidth()][img.getHeight()];
        
        int[] temp = {0, 0, 0};
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                raster.getPixel(x, y, temp);
                Pixel p = new Pixel(temp[0], temp[1], temp[2]);
                p.x = x;
                p.y = y;
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
    
    private class Pixel {
        public int x, y;
        public int r, g, b;
        public double L, A, B;

        private Pixel(int r, int g, int b) {
            x = y = 0;
            this.r = r;
            this.g = g;
            this.b = b;
            
            if (new_bias) {
                float[] tempF = {0, 0, 0};
                new Color(r, g, b).getColorComponents(
                                    ColorSpace.getInstance(ColorSpace.CS_CIEXYZ), 
                                    tempF);

                //"normalized" reference point.
                double Xn =  /*96.42;*/ 95.047;
                double Yn = 100.000;
                double Zn = /*82.49;*/ 108.883;

                double X = tempF[0];
                double Y = tempF[1];
                double Z = tempF[2];

                L = 166*f(Y/Yn) - 16;
                A = 500*(f(X/Xn) - f(Y/Yn));
                B = 200*(f(Y/Yn) - f(Z/Zn));
            }
        }

        private Pixel() {
        }
    }
    
    private static class PImage {
        Pixel[][] imageBuffer;
    }
}