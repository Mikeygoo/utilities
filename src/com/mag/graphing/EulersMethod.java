package com.mag.graphing;

import com.mag.parsing.CalculationParser;
import com.mag.parsing.Expression;
import com.mag.parsing.ParseException;
import com.mag.parsing.UnresolvedNameException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author michael
 */
public class EulersMethod {
    public static void main(String[] args) {
        String plotinput = JOptionPane.showInputDialog("Give a differential. y' = ...");
        
        Expression diffeq = null;
        
        try {
            diffeq = CalculationParser.interpretExpressionFromString(plotinput);
        } catch (ParseException ex) {
            Logger.getLogger(EulersMethod.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        String stepSizeString = JOptionPane.showInputDialog("Give a step size. h = ...");
        double stepSize = Double.parseDouble(stepSizeString);
        
        
        String finalString = JOptionPane.showInputDialog("Final point. x = ...");
        double finalX = Double.parseDouble(stepSizeString);
        
        String initialValueString = JOptionPane.showInputDialog("Give an x, y pair for an initial condition. x, y...");
        String[] initialValueSplit = initialValueString.split("(,[ ]*|[ ]+)");
        System.out.println(Arrays.toString(initialValueSplit));
        double initialX = Double.parseDouble(initialValueSplit[0]);
        double initialY = Double.parseDouble(initialValueSplit[1]);
        
        double x = initialX, y = initialY;
        
        for (; x <= finalX; x += stepSize) {
            HashMap<String, Double> map = new HashMap<String, Double>();
            map.put("x", x);
            map.put("y", y);
            
            double yprime = Double.NaN;
            
            try {
                yprime = diffeq.solve(map);
            } catch (UnresolvedNameException ex) {
                Logger.getLogger(EulersMethod.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            System.out.println(String.format("(%f, %f)", x, y));
            
            y = y + yprime * stepSize;
        }
        
        JOptionPane.showMessageDialog(null, String.format("(%f, %f)", x, y));
    }
}
