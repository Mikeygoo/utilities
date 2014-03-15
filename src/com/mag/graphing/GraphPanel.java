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
public class GraphPanel extends JPanel implements Transformable {
    private double xbl = -10, xbh = 10, xscale = 0.001; /* X draw-bounds low and high */
    private double xlineint = 1, ylineint = 1;
    private double xdl = -10, xdh = 10, ydl = -10, ydh = 10; /* X and Y window bounds. (X display low, x display high... */
    private HashMap<Expression, Color> exps = new HashMap<Expression, Color>();

    public GraphPanel() {
        setFocusable(true);
    }

    public void setFunctionsAndRedraw(Collection<? extends Expression> c) {
        //TODO: assign each function a color, and store in hashmap.
        exps.clear();

        for (Expression exp : c)
            exps.put(exp, getRandomColor());

        repaintViewport();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidthGraph(), getHeightGraph());
        g.setColor(Color.BLACK);

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

            for (Entry<Expression, Color> entry : exps.entrySet()) {
                g.setColor(entry.getValue());
                paintExpression(g, entry.getKey(), xmid, xfactor, ymid, yfactor);
            }

            g2d.setStroke(oldstroke);
            g2d.setColor(oldcolor);
        } catch (UnresolvedNameException nave) {
            System.out.println(nave);
            return;
        }
    }

    public void paintExpression(Graphics g, Expression e, double xmid, double xfactor, double ymid, double yfactor) throws UnresolvedNameException {
        HashMap<String, Double> h = new HashMap<String, Double>();
        double lastX = 0, lastY = 0;
        boolean hasBegan = false;

        for (double x = xbl; x < xbh; x += xscale) {
            h.put("x", Math.round(x / xscale) * xscale);
            double y = e.solve(h);

            //System.out.println("x = "+Math.round(x/xscale)*xscale+", y = "+y);
            if (Double.isInfinite(y) || Double.isInfinite(lastY) || Double.isNaN(y) || Double.isNaN(lastY))
                hasBegan = false;

            if (hasBegan) {
                double xt1 = xmid + x * xfactor, yt1 = ymid - y * yfactor, xt2 = xmid + lastX * xfactor, yt2 = ymid - lastY * yfactor;
                g.drawLine((int)xt1, (int)yt1, (int)xt2, (int)yt2);
            } else
                hasBegan = true;

            lastX = x;
            lastY = y;
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
