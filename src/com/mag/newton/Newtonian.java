package com.mag.newton;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author michael
 */
public class Newtonian extends JPanel implements MouseListener {
    public static int STEPS = 100;
    public static double G = .001 / STEPS;
    public static long DELTA = 1;
    
    public static void main(String[] args) throws InterruptedException {
        JFrame newtonFrame = new JFrame("Newton");
        newtonFrame.setSize(500, 500);
        
        Newtonian newton = new Newtonian();
        newton.setSize(500, 500);
        newtonFrame.add(newton);
        newtonFrame.addMouseListener(newton);
        newtonFrame.setVisible(true);
        
        final double BIGPLANETMASS = 100000000;
        final double LILPLANETMASS = 10000000;
        final int LILPLANETRAD     = 200;
        final double MOONMASS      = 10000;
        final int MOONRAD          = 20;
        
        double vMOON = sqrt(LILPLANETMASS*G/MOONRAD) + sqrt(BIGPLANETMASS*G/(MOONRAD+LILPLANETRAD));
        double vLIL = sqrt(BIGPLANETMASS*G/LILPLANETRAD) - vMOON*MOONMASS/LILPLANETMASS;
        double vBIG = -vLIL*LILPLANETMASS/BIGPLANETMASS - vMOON*MOONMASS/BIGPLANETMASS; //all the -'s are to keep the system's momentum = 0. the Center of Mass is ~(249, 249)
        
        newton.addPlanet(BIGPLANETMASS, 20, new Vec(0, vBIG), new Vec(250, 250));
        newton.addPlanet(LILPLANETMASS, 4, new Vec(0, vLIL), new Vec(250-LILPLANETRAD, 250));
        newton.addPlanet(MOONMASS, 1, new Vec(0, vMOON), new Vec(250-LILPLANETRAD-MOONRAD, 250));
        
        Thread.sleep(2000);
        while (true) {
            Thread.sleep(DELTA);
            for (int i = 0; i < STEPS; i++) {
                newton.logic(DELTA);
            }
            newton.repaint();
        }
    }
    
    private List<Planet> planets = Collections.synchronizedList(new ArrayList<Planet>());
    private Vec locationPressed = null;

    private void logic(long delta) {
        for (int i = 0; i < planets.size(); i++) {
            for (int j = 0; j < planets.size(); j++) {
                if (i == j) continue; //planets don't act on themselves.
                Planet x = planets.get(i);
                Planet y = planets.get(j);
                double dist = x.position.dist(y.position) + 0.001;
                if (dist < x.radius + y.radius) {
                    (x.mass < y.mass ? x : y).trash = true; //disgusting ternary lval
                }
                if (x.trash || y.trash)
                    continue;
                double accel = y.mass * G / (dist * dist);
                //System.out.println("An accel on the order of "+accel+" m/s²");
                x.velocity.addToScaled(x.position.normPointTowards(y.position), accel * delta / 1000.0);
            }
        }
        
        ListIterator<Planet> it = planets.listIterator();
        while (it.hasNext()) {
            Planet x = it.next();
            if (x.trash)
                it.remove();
            else {
                x.position.addToScaled(x.velocity, delta / 1000.0);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, 500, 500);
        for (Planet p : planets) {
            int x = (int) p.position.x, y = (int) p.position.y;
            g2.setColor(Color.WHITE);
            g2.fillOval(x - p.radius, y - p.radius, p.radius * 2, p.radius * 2);
            g2.setColor(Color.RED);
            g2.drawString(p.position.toString(), x+10, y+10);
        }
    }
    
    public void addPlanet(double mass, int radius, Vec velocity, Vec position) {
        Planet p = new Planet();
        p.mass = mass;
        p.radius = radius;
        p.velocity = velocity;
        p.position = position;
        planets.add(p);
    }

    private Planet getGreatestContributor(Vec x) {
        Planet greatest = null;
        double pot = 0;
        for (Planet p : planets) {
            double dist = x.dist(p.position) + 0.001;
            double newpot = p.mass * G / (dist * dist);
            if (newpot > pot) {
                pot = newpot;
                greatest = p;
            }
        }
        return greatest;
    }

    private static class Planet {
        double mass;
        int radius;
        boolean trash;
        Vec velocity = new Vec(), position = new Vec();
    }
    
    //stubs

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Click at " + e.getX() + ", "+ e.getY());
        Planet p = new Planet();
        p.mass = 1000;
        p.radius = 1;
        p.position = new Vec(e.getX(), e.getY());
        p.velocity = new Vec(0, 0);
        
        Planet sun = getGreatestContributor(p.position);
        //So it turns out p and sun are becoming the same position? why?
        p.velocity.addToScaled(p.position.normPointTowards(sun.position).perpendicular(), sqrt(sun.mass * G / (p.position.dist(sun.position) + 0.001)));
        //TODO: CHECK VELOCITY AND POS BEFORE RETURNING, SEE WHY IT'S DOING THIS...
        planets.add(p);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        locationPressed = new Vec(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Vec diff = new Vec(e.getX(), e.getY()).pointTowards(locationPressed);
        diff.scaleTo(.05);
        addPlanet(10, 3, diff, locationPressed);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}

class Vec {
    public double x,  y;
    
    public Vec() {
    }
    
    public Vec(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void addTo(Vec v) {
        x += v.x;
        y += v.y;
    }
    
    public void addToScaled(Vec v, double dt) {
        x += v.x * dt;
        y += v.y * dt;
    }
    
    public void normTo() {
        double scale = 1.0 / sqrt(x * x + y * y);
        x *= scale;
        y *= scale;
    }
    
    public Vec add(Vec v) {
        return new Vec(x + v.x, y + v.y);
    }
    
    //
    public Vec pointTowards(Vec to) {
        return new Vec(to.x - x, to.y - y);
    }
    
    public Vec normPointTowards(Vec to) {
        Vec r = new Vec(to.x - x, to.y - y);
        r.normTo();
        return r;
    }
    
    public Vec norm() {
        double scale = 1.0 / sqrt(x * x + y * y);
        return new Vec(x * scale, y * scale);
    }
    
    public double dist(Vec v) {
        double dx = x - v.x, dy = y - v.y;
        return sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format("(%.3f, %.3f)", x, y);
    }

    public void scaleTo(double d) {
        x *= d;
        y *= d;
    }

    public Vec perpendicular() {
        return new Vec(-y, x);
    }
}
