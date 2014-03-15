package com.mag.cookies;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Calendar;

/**
 *
 * @author Kevin
 */
public class GoldenCookieFinder {
    public static void main(String[] args) throws AWTException, InterruptedException {
        final int TIME = 1000;
        Robot r = new Robot();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) d.getWidth(), height = (int) d.getHeight();
        //int referenceCookie = new Color(194, 159, 76).getRGB();
        System.out.println("Started at " + getTimeString());
        //simulateMove(r, 100, 100);
        int[] c = null;
        int cookienum = 0;

        while (true) {
            BufferedImage cap = r.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            Raster ras = cap.getData();
            outer: for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++) {
                    c = ras.getPixel(x, y, c);

                    if ((c[0] == 194 && c[1] == 159 && c[2] == 76) ||
                            (c[0] == 0 && c[1] == 133 && c[2] == 149)) {
                        simulateMove(r, x, y);
                        r.mousePress(InputEvent.BUTTON1_MASK);
                        r.mouseRelease(InputEvent.BUTTON1_MASK);
                        System.out.println((++cookienum) + ": Found a " + (c[0] == 194 ? "ref match" : "elder pact") + " at " + x + ", " + y + " at " + getTimeString());
                        break outer;
                    }

                    //System.out.println(x + " " + y);
                }

            //System.out.println(".");
            Thread.sleep(TIME);
        }
    }

    private static String getTimeString() {
        Calendar c = Calendar.getInstance();
        return String.format("%d/%d at %02d:%02d",
                             c.get(Calendar.MONTH) + 1,
                             c.get(Calendar.DAY_OF_MONTH),
                             c.get(Calendar.HOUR_OF_DAY),
                             c.get(Calendar.MINUTE));
    }

    private static void simulateMove(Robot r, int x2, int y2) throws InterruptedException {
        int x1 = MouseInfo.getPointerInfo().getLocation().x, y1 = MouseInfo.getPointerInfo().getLocation().y;
        long starttime = System.currentTimeMillis();

        while (Math.abs(x1 - x2) > 5 || Math.abs(y1 - y2) > 5) {
            if (System.currentTimeMillis() - starttime > 11000) { //11 second timeout
                System.out.println("Timeout on Mouse-Click.");
                break;
            }

            if (x1 > x2)
                x1 -= (int)(Math.random() * 5);
            else
                x1 += (int)(Math.random() * 5);

            if (y1 > y2)
                y1 -= (int)(Math.random() * 5);
            else
                y1 += (int)(Math.random() * 5);

            r.mouseMove(x1, y1);
            Thread.sleep(10);
            x1 = MouseInfo.getPointerInfo().getLocation().x;
            y1 = MouseInfo.getPointerInfo().getLocation().y;
        }

        r.mouseMove(x2, y2);
    }
}
