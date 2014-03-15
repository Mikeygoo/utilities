/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mag.parsing;

/**
 *
 * @author admin
 */
public class ParserMain {
    public static void main(String[] args) throws ParseException, UnresolvedNameException {
        System.out.println(CalculationParser.interpretExpressionFromString("-y/-x"));
    }
}
