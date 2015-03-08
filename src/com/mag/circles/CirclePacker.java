package com.mag.circles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author michael
 */
public class CirclePacker extends JFrame {
    public static Random r = new Random(0);
    public static int DWIDTH = 5000; //width of the underlying buffer
    public static int DHEIGHT = 5000; //height of the underlying buffer
    public static double DINFLATE = 1; //how much the radius expands per step
    public static int DMAXCIRCLES = 50000; //maximum amount of circles before clearing half of the circles
    public static int DFOCI = 10; //number of simultaneously expanding circles
    public static int DMAXREPEATS = 5; //(1 = minimum, no repeats) maximum repeats of clearing half before beginning over again
    public static double DMINRAD = 1;
    public static boolean DPERMUTE = true; //Should the circles move around to accomodate more expansion?
    public static boolean DALLATONCE = false; //Should the circles all begin at once (true), or each focus begin when the last one ends?
    public static boolean DBLACKEN = true; //Should the circles blacken after a "repeat"?
    public static boolean DPOPMINIMUM = true; //Should circles that don't make >= the size DMINRAD be "popped" (removed?)

    public static double[] XPOSES = {1, 0, -1}; // I didn't want to run through all of the
    public static double[] YPOSES = {1, 0, -1}; // permutations of X and Y movements for DPERMUTE mode.

    public static void main(String[] args) throws InterruptedException {
        final CirclePacker cp = new CirclePacker();
        cp.setSize(700, 700); //here's the actual window size, not (DWIDTH, DHEIGHT)...
        cp.setVisible(true);

        while (true) {
            Thread.sleep(15);
            for (int i = 0; i < 15; i++) {
                cp.logic(); //15 logical ticks before I draw. This is arbitrary, though.
            }
            cp.repaint(); //draw onto the actual visual window.
        }
    }

    private BufferedImage bi = new BufferedImage(DWIDTH, DHEIGHT, BufferedImage.TYPE_INT_RGB); //underlying buffer.
    private ArrayList<Circle> others = new ArrayList<>(); //all of the old circles are here.
    private Circle[] foci = new Circle[DFOCI]; //all of the currently-expanding circles.
    private Ellipse2D.Double representation = new Ellipse2D.Double(); //I just use this to draw my circles.
    private double maxrad = 0; //maximum radius per "repeat"
    private int repeats = 0; //number of repeats currently...

    public CirclePacker() {
        Graphics g = bi.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, DWIDTH, DHEIGHT);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        //next 2 lines: is this necessary??
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        g.drawImage(bi, 0, 20, getWidth(), getHeight() - 20, this);
    }

    public void logic() { //let's do one tick
        boolean allNull = true;

        for (int i = 0; i < DFOCI; i++) { //for each circle,
            /* We have 2 modes, "ALL AT ONCE" and "NOT ALL AT ONCE".
             * If we're in all-at-once mode, then we have to wait until all
             * of the circles get stuck before we make new circles.
             * If we're not in all-at-once mode, then we can call sublogic
             * on the null circle, and it will automatically generate a new one
             * for us to work with.
             */
            if (foci[i] != null || !DALLATONCE) { //skip null circle unless we're not in all-at-once mode.
                allNull = false;
                sublogic(i);
            }
        }

        if (allNull && DALLATONCE) { //if all of them are null, let's generate new circles (in all-at-once mode).
            for (int i = 0; i < DFOCI; i++) {
                generateCircle(i);
            }
        }

        /* If we have reached the maximum amount of circles per repeat, we can
         * then call totallyFilled, which does more stuff!
         */
        if (others.size() > DMAXCIRCLES) {
            totallyFilled();
            return;
        }
    }

    public void sublogic(int index) {
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        // We talked about this above...
        if (foci[index] == null) {
            if (DALLATONCE)
                return;
            else
                generateCircle(index);
        }
        
        Circle subject = foci[index];

        drawCircle(subject, g2);

        //let's inflate the circle now...
        subject.inflate();

        //if it's out of bounds, then:
        if (!inBounds(index)) {
            /* If we're not permuting, we can call finishCircle,
             * which adds the old subject circle to the others list and
             * sets the subject circle to null in the foci[] array.
             */
            if (!DPERMUTE) {
                finishCircle(index);
                return;
            }
            
            // HOWEVER, if we ARE permuting:
            
            //let's permute the circle's X and Y to get a new position...
            //first, let's store the old circle's info.
            double originalRad = subject.rad - DINFLATE;
            double originalX = subject.x;
            double originalY = subject.y;
            boolean corrected = false;

            /* This loop looks for a new circle position that is within the
             * boundaries and that does not intersect any other circles.
             * I'm pretty sure it prefers the top-right corner first...
             */
            outer:
            for (double xi : XPOSES) {
                for (double yj : YPOSES) {
                    subject.x = originalX + DINFLATE * xi;
                    subject.y = originalY + DINFLATE * yj;

                    if (inBounds(index)) {
                        corrected = true;
                        break outer;
                    }
                }
            }

            if (!corrected) {
                //if we couldn't fit the circle anywhere, then too bad...
                finishCircle(index);
            } else {
                //this is bad code duplication... but whatever.
                //We need to get rid of the old circle first.
                representation.setFrame(originalX - originalRad, originalY - originalRad, originalRad * 2, originalRad * 2);
                g2.setColor(Color.WHITE);
                g2.fill(representation);

                drawCircle(subject, g2);
            }
        }
    }

    private void drawCircle(Circle subject, Graphics2D g2) {
        //let's draw the circle...
        representation.setFrame(subject.x - subject.rad, subject.y - subject.rad, subject.rad * 2, subject.rad * 2);
        g2.setColor(subject.c);
        g2.fill(representation);
    }

    private void generateCircle(int index) {
        //This method generates a new circle.
        //TODO: rewrite this more gracefully.
        outer:
        while (foci[index] == null) {
            //let's look for a new coordinate.
            double randx = r.nextDouble() * DWIDTH;
            double randy = r.nextDouble() * DHEIGHT;

            for (Circle c : others) {
                if (c.within(randx, randy)) {
                    continue outer;
                }
            }

            for (Circle c : foci) {
                if (c != null && c.within(randx, randy)) {
                    continue outer;
                }
            }

            foci[index] = new Circle(randx, randy, new Color((int) (r.nextDouble() * Integer.MAX_VALUE)));
        }
    }

    private boolean inBounds(int index) {
        //checks if the subject circle is in bounds.
        Circle subject = foci[index];

        if (!subject.inBounds()) {
            return false;
        }

        for (Circle c : others) {
            if (c.intersects(subject)) {
                return false;
            }
        }

        for (Circle c : foci) {
            if (c != null && c != subject && c.intersects(subject)) {
                if (DPOPMINIMUM && c.rad < DMINRAD && subject.rad >= DMINRAD)
                    continue;
                return false;
            }
        }

        return true;
    }

    private void finishCircle(int index) {
        //We're done here.
        Circle subject = foci[index];
        
        if (DPOPMINIMUM && subject.rad < DMINRAD) {
            subject.c = Color.WHITE;
            drawCircle(subject, (Graphics2D) bi.getGraphics());
            foci[index] = null;
            return;
        }
        
        subject.deflate(); //step it back one notch, since it has expanded too much.
        
        if (maxrad < subject.rad)
            maxrad = subject.rad;
        
        others.add(subject);
        foci[index] = null;
    }

    private void totallyFilled() {
        repeats++;

        //if we're not at the maximum amount of repeats...
        if (repeats < DMAXREPEATS) {
            //Remove half of the circles!
            for (int i = 0; i < DMAXCIRCLES / 2; i++) {
                int j = (int) (r.nextDouble() * others.size());
                others.remove(j);
            }

            System.out.printf("%2.0f, ", maxrad);
            maxrad = 0;

            //let's clean the slate.
            Graphics2D g2 = (Graphics2D) bi.getGraphics();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, DWIDTH, DHEIGHT);

            for (Circle subject : others) {
                //if BLACKEN mode is on, then give all of the old circles the color black...
                if (DBLACKEN)
                    subject.c = Color.BLACK;
                
                drawCircle(subject, g2);
            }

            //let's not forget our foci.
            //In retrospect, this is actually kinda bad.
            for (Circle subject : foci) {
                if (subject == null) {
                    continue;
                }
                
                if (DBLACKEN) //I really SHOULDN'T blacken my old foci... Well. Whatevs.
                    subject.c = Color.BLACK;

                drawCircle(subject, g2);
            }
        } else {
            //LET'S CLEAR EVERYTHING, if we ran out of repeats.
            System.out.printf("%2.0f\n", maxrad);
            
            others.clear();

            for (int i = 0; i < DFOCI; i++) {
                foci[i] = null;
            }

            Graphics2D g2 = (Graphics2D) bi.getGraphics();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, DWIDTH, DHEIGHT);

            maxrad = 0;
            repeats = 0;

            System.gc(); //let's garbage collect all of the old circles. 
            //Probably not necessary, though.
        }
    }

    public void writeImage(String identity) {
        try {
            ImageIO.write(bi, "png", new File("prettypicture_" + identity + ".png"));
        } catch (IOException ex) {
            Logger.getLogger(CirclePacker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //This should all be pretty basic.
    public static class Circle {

        Color c;
        double x, y;
        double rad;

        public Circle(double x, double y, Color c) {
            this.x = x;
            this.y = y;
            this.c = c;
            this.rad = 0.01;
        }

        public boolean within(double px, double py) {
            //dist2 is distance-squared, since I don't really need to take a square root.
            double dist2 = (x - px) * (x - px) + (y - py) * (y - py);
            return rad * rad - dist2 > 0; //same as rad² > dist²...
        }

        public boolean intersects(Circle other) {
            double dist2 = (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y);
            return (rad + other.rad) * (rad + other.rad) - dist2 > 0;
            /* this basically checks whether the distance between the two
             * centers is greater than the sum of their radii, which is equivalent
             * to whether they are intersecting or not.
             */
        }

        public boolean inBounds() {
            return x > rad && DWIDTH - x > rad
                    && y > rad && DHEIGHT - y > rad;
        }

        private void inflate() {
            rad += DINFLATE;
        }

        private void deflate() {
            rad -= DINFLATE;
        }
    }
}
