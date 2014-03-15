package com.mag.parsing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author michael
 */
public class CalculationParser {
    private static final int PLUS_PRECEDENCE = 2; //+
    private static final int SUBT_PRECEDENCE = 2; //-
    private static final int IMMT_PRECEDENCE = 3; //implicit multiplication
    private static final int MULT_PRECEDENCE = 4; //*
    private static final int DIVI_PRECEDENCE = 4; //division
    private static final int EXPO_PRECEDENCE = 5;

    public static Expression interpretExpressionFromString(String s) throws ParseException {
        Deque<String> strings = new LinkedList<String>();

        int index = 0;
        char[] array = s.toCharArray();

        while (index < array.length) {
            if (Character.isDigit(array[index])) {
                String temp = "" + array[index];
                index++;

                while (true) {
                    if (index < array.length && (Character.isDigit(array[index]) || array[index] == '.')) {
                        temp += array[index];
                    } else
                        break;

                    index++;
                }

                strings.add(temp);
            } else if (Character.isJavaIdentifierStart(array[index])) {
                String temp = "" + array[index];
                index++;

                while (true) {
                    if (index < array.length && Character.isAlphabetic(array[index])) {
                        temp += "" + array[index];
                    } else
                        break;

                    index++;
                }

                strings.add(temp);
            } else {
                if (array[index] == ' ') {
                    index++;
                    continue;
                }

                String temp = "" + array[index];
                index++;
                strings.add(temp);
            }
        }

        BasicNode b = interpretExpression(strings, Integer.MIN_VALUE);

        if (b.hasNull())
            throw new ParseException("An error occurred while parsing the expression.");

        return b;
    }

    private static BasicNode interpretExpression(Deque<String> strings, int precedence) throws ParseException {
        BasicNode e = null;
        boolean unaryNegative = false;

        if ("-".equals(strings.peekFirst())) {
            strings.pollFirst();
            unaryNegative = true;
        }

        if ("(".equals(strings.peekFirst())) {
            strings.pollFirst();
            e = interpretExpression(strings, Integer.MIN_VALUE);

            if (unaryNegative) {
                e = new SubtractionNode(new NumberNode(0), e);
                unaryNegative = false;
            }

            String polled = strings.pollFirst();

            if (polled == null)
                return e;

            if (!")".equals(polled)) {
                throw new ParseException("Unclosed parenthesis. Expected '(', got '" + polled + "'.");
            }
        }

        outer: while (true) {
            String str = strings.pollFirst();

            if ("(".equals(str)) {
                if (e != null) {
                    if (precedence <= IMMT_PRECEDENCE) {
                        BasicNode b = interpretExpression(strings, Integer.MIN_VALUE);
                        e = new MultiplicationNode(e, b);
                    } else {
                        strings.offerFirst("(");
                        return e;
                    }
                } else {
                    e = interpretExpression(strings, Integer.MIN_VALUE);
                }

                String polled = strings.pollFirst();

                if (polled == null)
                    break outer;

                if (!")".equals(polled)) {
                    throw new ParseException("Unclosed parenthesis. Expected ')', got '" + polled + "'.");
                }

                continue;
            }

            if (str == null) {
                if (e == null)
                    throw new ParseException("Expected input, got nothing.");

                break outer;
            } else if (str.matches("[0-9]+(.[0-9]+)?")) {
                if (e != null) {
                    strings.offerFirst(str);

                    if (precedence <= IMMT_PRECEDENCE) {
                        BasicNode b = interpretExpression(strings, IMMT_PRECEDENCE);
                        e = new MultiplicationNode(e, b);
                    } else
                        return e;
                } else {
                    e = new NumberNode(Double.parseDouble(str));
                }

                continue;
            } else if (str.matches("[a-zA-Z]")) {
                if (e != null) {
                    strings.offerFirst(str);

                    if (precedence <= IMMT_PRECEDENCE) {
                        BasicNode b = interpretExpression(strings, IMMT_PRECEDENCE);
                        e = new MultiplicationNode(e, b);
                    } else
                        return e;
                } else {
                    e = new VariableNode(str);
                }

                continue;
            } else {
                if (str.length() == 1) {
                    char op = str.charAt(0);

                    switch (op) {
                    case '+': {
                        if (PLUS_PRECEDENCE > precedence) {
                            e = new AdditionNode(e, interpretExpression(strings, PLUS_PRECEDENCE));
                        } else {
                            strings.offerFirst(str);
                            break outer;
                        }
                    }
                    break;

                    case '-': {
                        if (SUBT_PRECEDENCE > precedence) {
                            e = new SubtractionNode(e != null ? e : new NumberNode(0), interpretExpression(strings, SUBT_PRECEDENCE));
                        } else {
                            strings.offerFirst(str);
                            break outer;
                        }
                    }
                    break;

                    case '/': {
                        if (DIVI_PRECEDENCE > precedence) {
                            e = new DivisionNode(e, interpretExpression(strings, DIVI_PRECEDENCE));
                            //TODO: Division omfg
                        } else {
                            strings.offerFirst(str);
                            break outer;
                        }
                    }
                    break;

                    case '*': {
                        if (MULT_PRECEDENCE > precedence) {
                            e = new MultiplicationNode(e, interpretExpression(strings, MULT_PRECEDENCE));
                        } else {
                            strings.offerFirst(str);
                            break outer;
                        }
                    }
                    break;

                    case '^': {
                        if (EXPO_PRECEDENCE >= precedence) {
                            e = new ExponentNode(e, interpretExpression(strings, EXPO_PRECEDENCE));
                        } else {
                            strings.offerFirst(str);
                            break outer;
                        }
                    }
                    break;

                    case ')':
                    case ',': {
                        strings.offerFirst(str);
                        break outer;
                    }

                    default: {

                    } break;
                    }
                } else {
                    if ("(".equals(strings.peekFirst())) {
                        ArrayList<Expression> exps = new ArrayList<Expression>();

                        do {
                            strings.pollFirst();
                            exps.add(interpretExpression(strings, precedence));
                        } while (strings.peekFirst() != null && ",".equals(strings.peekFirst()));

                        String polled = strings.pollFirst();

                        if (!")".equals(polled)) {
                            throw new ParseException("Unclosed parenthesis. Expected '(', got '" + polled + "'.");
                        }

                        e = new FunctionNode(str, exps);
                    } else {
                        throw new ParseException("Expected '(', got '" + strings.peekFirst() + "' after function call '" + str + "'.");
                    }
                }
            }
        }


        if (unaryNegative) {
            e = new SubtractionNode(new NumberNode(0), e);
        }

        return e;
    }

    private static class AdditionNode extends BasicNode {
        private BasicNode l, r;

        public AdditionNode(BasicNode l, BasicNode r) {
            this.l = l;
            this.r = r;
        }

        @Override
        protected double toNumberValue(Map<String, Double> vars) throws UnresolvedNameException {
            return l.toNumberValue(vars) + r.toNumberValue(vars);
        }

        @Override
        protected boolean hasNull() {
            return (l == null | r == null) || (l.hasNull() || r.hasNull());
        }

        @Override
        public String toString() {
            return "(" + l.toString() + "+" + r.toString() + ")";
        }
    }

    private static class SubtractionNode extends BasicNode {
        private BasicNode l, r;

        public SubtractionNode(BasicNode l, BasicNode r) {
            this.l = l;
            this.r = r;
        }

        @Override
        protected double toNumberValue(Map<String, Double> vars) throws UnresolvedNameException {
            return l.toNumberValue(vars) - r.toNumberValue(vars);
        }

        @Override
        protected boolean hasNull() {
            return (l == null | r == null) || (l.hasNull() || r.hasNull());
        }

        @Override
        public String toString() {
            return "(" + l.toString() + "-" + r.toString() + ")";
        }
    }

    private static class DivisionNode extends BasicNode {
        private BasicNode l, r;

        public DivisionNode(BasicNode l, BasicNode r) {
            this.l = l;
            this.r = r;
        }

        @Override
        protected double toNumberValue(Map<String, Double> vars) throws UnresolvedNameException {
            double lval = l.toNumberValue(vars), rval = r.toNumberValue(vars);

            if (rval == 0 || Double.isNaN(rval))
                return Double.NaN;

            return lval / rval;
        }

        @Override
        protected boolean hasNull() {
            return (l == null | r == null) || (l.hasNull() || r.hasNull());
        }

        @Override
        public String toString() {
            return "(" + l.toString() + "/" + r.toString() + ")";
        }
    }

    private static class MultiplicationNode extends BasicNode {
        private BasicNode l, r;

        public MultiplicationNode(BasicNode l, BasicNode r) {
            this.l = l;
            this.r = r;
        }

        @Override
        protected double toNumberValue(Map<String, Double> vars) throws UnresolvedNameException {
            return l.toNumberValue(vars) * r.toNumberValue(vars);
        }

        @Override
        protected boolean hasNull() {
            return (l == null | r == null) || (l.hasNull() || r.hasNull());
        }

        @Override
        public String toString() {
            return "(" + l.toString() + "*" + r.toString() + ")";
        }
    }

    private static class ExponentNode extends BasicNode {
        private BasicNode l, r;

        public ExponentNode(BasicNode l, BasicNode r) {
            this.l = l;
            this.r = r;
        }

        @Override
        protected double toNumberValue(Map<String, Double> vars) throws UnresolvedNameException {
            return Math.pow(l.toNumberValue(vars), r.toNumberValue(vars));
        }

        @Override
        protected boolean hasNull() {
            return (l == null | r == null) || (l.hasNull() || r.hasNull());
        }

        @Override
        public String toString() {
            return "(" + l.toString() + "^" + r.toString() + ")";
        }
    }

    private static class NumberNode extends BasicNode {
        private double d;

        public NumberNode(double d) {
            this.d = d;
        }

        @Override
        protected double toNumberValue(Map<String, Double> vars) {
            return d;
        }

        @Override
        protected boolean hasNull() {
            return false;
        }

        @Override
        public String toString() {
            if (Math.floor(d) == d)
                return (long)(d) + "";
            else
                return d + "";
        }
    }

    private static class VariableNode extends BasicNode {
        private String varname;

        public VariableNode(String varname) {
            this.varname = varname.toLowerCase();
        }

        @Override
        protected double toNumberValue(Map<String, Double> vars) throws UnresolvedNameException {
            Double d = vars.get(varname);

            if (d == null)
                throw new UnresolvedNameException(varname);

            return d;
        }

        @Override
        protected boolean hasNull() {
            return false;
        }

        @Override
        public String toString() {
            return varname;
        }
    }

    private static class FunctionNode extends BasicNode {
        String function = "";
        ArrayList<Expression> ar = new ArrayList<Expression>();

        public FunctionNode(String name, Collection<? extends Expression> params) {
            function = name;
            ar.addAll(params);
        }

        @Override
        protected double toNumberValue(Map<String, Double> vars) throws UnresolvedNameException {
            double[] doubles = new double[ar.size()];

            for (int i = 0; i < doubles.length; i++)
                doubles[i] = ar.get(i).solve(vars);

            return MathFunctionModule.executeFunction(function.toLowerCase(), doubles);
        }

        @Override
        protected boolean hasNull() {
            boolean t = false;

            for (Expression e : ar)
                t = t || e == null;

            return t || function == null;
        }

        @Override
        public String toString() {
            String s = "";

            for (Expression e : ar)
                s += ", " + e.toString();

            return function + "(" + s.substring(2) +  ")";
        }
    }
}
