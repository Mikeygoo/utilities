package com.mag.parsing;

/**
 *
 * @author michael
 */
public class MathFunctionModule {
    public static double executeFunction(String function, double[] doubles) throws UnresolvedNameException {
        //System.out.println("Solving "+function+" for "+Arrays.toString(doubles));
        switch (function) {
        case "sin": {
            if (doubles.length != 1) throw new UnresolvedNameException("sin()");

            return Math.sin(doubles[0]);
        }

        case "cos": {
            if (doubles.length != 1) throw new UnresolvedNameException("cos()");

            return Math.cos(doubles[0]);
        }

        case "tan": {
            if (doubles.length != 1) throw new UnresolvedNameException("tan()");

            return Math.tan(doubles[0]);
        }

        case "abs": {
            if (doubles.length != 1) throw new UnresolvedNameException("abs()");

            return Math.abs(doubles[0]);
        }

        case "asin": {
            if (doubles.length != 1) throw new UnresolvedNameException("asin()");

            return Math.asin(doubles[0]);
        }

        case "acos": {
            if (doubles.length != 1) throw new UnresolvedNameException("acos()");

            return Math.acos(doubles[0]);
        }

        case "atan": {
            if (doubles.length != 1) throw new UnresolvedNameException("atan()");

            return Math.atan(doubles[0]);
        }

        case "logb": {
            if (doubles.length != 2) throw new UnresolvedNameException("logb()");

            return Math.log(doubles[1]) / Math.log(doubles[0]);
        }
        }

        throw new UnresolvedNameException(function + "()");
    }
}
