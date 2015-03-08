package com.mag.graphing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.swing.JPanel;
import com.mag.parsing.Expression;
import com.mag.parsing.UnresolvedNameException;

/**
 *
 * @author michael
 */
public class SlopePanel extends JPanel implements Transformable {
    private double xbl = -10, xbh = 10, xscale = 0.001; /* X draw-bounds low and high */
    private double xlineint = 1, ylineint = 1;
    private double xdl = -10, xdh = 10, ydl = -10, ydh = 10; /* X and Y window bounds. (X display low, x display high... */
    private Expression diffeq;

    public SlopePanel() {
        setFocusable(true);
    }

    public void setFunctionAndRedraw(Expression e) {
        //TODO: assign each function a color, and store in hashmap.
        diffeq = e;

        repaintViewport();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidthGraph(), getHeightGraph());
        g.setColor(Color.GRAY);

        try {
            double xfactor = getWidthGraph() / (xdh - xdl), yfactor = getHeightGraph() / (ydh - ydl); //a normalization factor. always do xposition*xfactor + xmid
            double xmid = getWidthGraph() - (xdh) * xfactor, ymid = getHeightGraph() - (ydh) * yfactor; //y axis is xmid, x axis is ymid. yeah...

            //drawing the lines.
            for (double xline = 0; xline < xdh; xline += xlineint) {
                g.drawLine((int)(xmid + xline * xfactor), 0, (int)(xmid + xline * xfactor), getHeightGraph());
            }

            for (double xline = 0; xline > xdl; xline -= xlineint) {
                g.drawLine((int)(xmid + xline * xfactor), 0, (int)(xmid + xline * xfactor), getHeightGraph());
            }

            for (double yline = 0; yline < ydh; yline += ylineint) {
                g.drawLine(0, (int)(ymid + yline * yfactor), getWidthGraph(), (int)(ymid + yline * yfactor));
            }

            for (double yline = 0; yline > ydl; yline -= ylineint) {
                g.drawLine(0, (int)(ymid + yline * yfactor), getWidthGraph(), (int)(ymid + yline * yfactor));
            }

            Graphics2D g2d = (Graphics2D) g;
            Stroke oldstroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(2));
            g.drawLine(0, (int)(ymid), getWidthGraph(), (int)(ymid));
            g.drawLine((int)(xmid), 0, (int)(xmid), getHeightGraph());
            Color oldcolor = g.getColor();

            
            g.setColor(Color.RED);
            paintSlopeField(g, xmid, xfactor, ymid, yfactor);

            g2d.setStroke(oldstroke);
            g2d.setColor(oldcolor);
        } catch (UnresolvedNameException nave) {
            System.out.println(nave);
            return;
        }
    }

    public void paintSlopeField(Graphics g, double xmid, double xfactor, double ymid, double yfactor) throws UnresolvedNameException {
        HashMap<String, Double> h = new HashMap<String, Double>();

        for (double x = xbl; x < xbh; x += xlineint) {
            for (double y = xbl; y < xbh; y += ylineint) {
                h.put("x", Math.round(x / xscale) * xscale);
                h.put("y", Math.round(y / xscale) * xscale);
                double yprime = diffeq.solve(h);

                //System.out.println("x = "+Math.round(x/xscale)*xscale+", y = "+y);
                if (Double.isInfinite(yprime) || Double.isNaN(yprime))
                    continue;
                double length = 0.5;
                double deltaX = length / Math.sqrt(yprime * yprime + 1);
                double deltaY = length * yprime / Math.sqrt(yprime * yprime + 1);
                
                double x1 = x - deltaX, x2 = x + deltaX;
                double y1 = y - deltaY, y2 = y + deltaY;
                
                double xt1 = xmid + x1 * xfactor, yt1 = ymid - y1 * yfactor, xt2 = xmid + x2 * xfactor, yt2 = ymid - y2 * yfactor;
                g.drawLine((int)xt1, (int)yt1, (int)xt2, (int)yt2);
            }
        }
    }

    public void setGraphBoundaries(double deflow, double defhigh, double defdx) {
        xbl = xdl = deflow;
        xbh = xdh = defhigh;
        xscale = defdx;
        repaintViewport();
    }

    public void zoomPercent(double percentChange) {
        percentChange = Math.floor(percentChange * 5) / 5.0;
        xdl = xbl *= percentChange;
        xdh = xbh *= percentChange;
        ydl *= percentChange;
        ydh *= percentChange;
        repaintViewport();
    }

    @Override
    public void transformBoundaries(double dx, double dy) {
        double xfactor = getWidthGraph() / (xdh - xdl), yfactor = getHeightGraph() / (ydh - ydl); //a normalization factor. always do xposition*xfactor + xmid
        xbl = xdl += dx / xfactor;
        xbh = xdh += dx / xfactor;
        ydl += dy / yfactor;
        ydh += dy / yfactor;
        repaintViewport();
    }

    private int getHeightGraph() {
        return getHeight();
    }

    private int getWidthGraph() {
        return getWidth();
    }

    private void repaintViewport() {
        repaint();
    }

    ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(new Color[] {Color.RED, Color.GREEN, Color.BLUE}));
    Iterator<Color> it = colors.iterator();
    private Color getRandomColor() {
        if (it.hasNext())
            return it.next().darker();

        return new Color((int)(Math.random() * Integer.MAX_VALUE * 2 - Integer.MAX_VALUE));
    }
}
