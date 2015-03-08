package com.mag.parsing;


import com.mag.parsing.CalculationParser;
import com.mag.parsing.Expression;
import com.mag.parsing.ParseException;
import com.mag.parsing.UnresolvedNameException;
import java.util.HashMap;
import java.util.Scanner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author michael
 */
public class ParserTester {
    public static void main(String[] args) throws ParseException, UnresolvedNameException {
        while (true) {
            Scanner s = new Scanner(System.in);
            String a = s.nextLine();
            String b = s.nextLine();
            Expression ae = CalculationParser.interpretExpressionFromString(a);
            Expression be = CalculationParser.interpretExpressionFromString(b);
            System.out.println(ae + " = " + be);
            for (String r : new String[] {"0", "1/6", "1/4", "1/3", "1/2", "2/3", "3/4", "5/6", "1", "7/6", "5/4", "4/3", "3/2", "5/3", "7/4", "11/6", "2"}) {
                double num = Math.PI * CalculationParser.interpretExpressionFromString(r).solve(null);
                HashMap<String, Double> x = new HashMap<>();
                x.put("x", num);
                double aev = ae.solve(x);
                double bev = be.solve(x);
                if (Math.abs(aev - bev) < 0.1) {
                    System.out.printf("pi*%s: %f = %f\n", r, aev, bev);
                }
            }
            System.out.println();
        }
    }
}
