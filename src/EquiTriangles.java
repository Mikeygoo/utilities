
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class EquiTriangles extends JFrame {

    public static void main(String[] args) {
        EquiTriangles e = new EquiTriangles();
        e.setSize(500, 500);
        e.setVisible(true);
    }

    /*
     * I don't like making java.lang.Math calls.
     * They're weird.
     */
    public static int abs(int x) {
        return (x > 0 ? 1 : -1) * x;
    }

    @Override
    public void paint(Graphics g) {
        //you can change the coordinates.
        drawTriangle(g, 250, 100, 100, 250, 400, 250);
    }

    public void drawTriangle(Graphics g, int tX, int tY, int lX, int lY, int rX, int rY) {
        //t = top of triangle, l = left, r = right (points)
        //midpoints between t, l, and r.
        int tlX = (tX + lX) / 2, tlY = (tY + lY) / 2;
        int trX = (tX + rX) / 2, trY = (tY + rY) / 2;
        int lrX = (lX + rX) / 2, lrY = (lY + rY) / 2;

        //draw the actual triangle.
        g.setColor(Color.BLACK);
        g.drawLine(tX, tY, lX, lY);
        g.drawLine(lX, lY, rX, rY);
        g.drawLine(rX, rY, tX, tY);

        //just a limitation to not draw too small. (pointless to draw a triangle of length 0.)
        if (abs(rX - lX) < 5 || abs(rY - tY) < 5)
            return;

        //recurse on subtriangles to left and right.
        drawTriangle(g, tlX, tlY, lX, lY, lrX, lrY);
        drawTriangle(g, trX, trY, lrX, lrY, rX, rY);
    }
}
