package com.mag.numerology;

import com.mag.numerology.NumberSystem.Num;
import java.util.ArrayList;

public abstract class NumberSystem<N extends Num<N, D>, D> {
    protected abstract D getZeroDigit();
    protected abstract N getZeroNumber();
    protected abstract N getNegativeOneNumber();
    protected abstract N shiftSignificant(N d);
    protected abstract D addDig(D a, D b, D c);
    protected abstract D addCarry(D a, D b, D c);
    protected abstract D multDig(D a, D b, D c);
    protected abstract D multCarry(D a, D b, D c);
    protected abstract D[] getEmptyArrayForCast();
    protected abstract N create(D[] a);
    protected abstract D getDigitFromChar(char c);
    protected abstract String toDecimalRepresentation(N n);
    protected abstract N getDecimalNumber(int i);
    
    public static abstract class Num<N extends Num<N, D>, D> {
        private NumberSystem<N, D> ns;
        private D[] digits;
        private int significance;

        public Num(NumberSystem<N,D> ns, D[] digits) {
            this.ns = ns;
            this.digits = digits;
            this.significance = digits.length;
            
            for (int i = significance - 1; i >= 1; i--) {
                if (digits[i].equals(ns.getZeroDigit()))
                    this.significance--;
                else
                    break;
            }
        }
        
        public int significance() {
            return significance;
        }
        
        public D getDigit(int i) {
            if (i >= digits.length)
                return ns.getZeroDigit();
            return digits[i];
        }
        
        private void setDigit(int i, D d) {
            digits[i] = d;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            
            for (int i = significance() - 1; i >= 0; i--)
                sb.append(digits[i].toString());
            
            return sb.toString();
        }
        
        public String toDecimalRepresentation() {
            return ns.toDecimalRepresentation((N) this);
        }
    }

    public NumberSystem() {
    }
    
    public N add(N a, N b) {
        int sig = Math.max(a.significance(), b.significance());
        D carry = getZeroDigit();
        ArrayList<D> digits = new ArrayList<D>();
        for (int i = 0; i < sig || !carry.equals(getZeroDigit()); i++) {
            D dig = addDig(a.getDigit(i), b.getDigit(i), carry);
            carry = addCarry(a.getDigit(i), b.getDigit(i), carry);
            digits.add(dig);
        }
        digits.trimToSize();
        return create(digits.toArray(getEmptyArrayForCast()));
    }
    
    public N multiply(N a, N b) {
        N resultant = getZeroNumber();
        for (int i = a.significance(); i >= 0; i--) {
            resultant = add(shiftSignificant(resultant), 
                            multiplyTrivially(a.getDigit(i), b));
        }
        return resultant;
    }
    
    public N subtract(N a, N b) {
        return add(a, multiply(b, getNegativeOneNumber()));
    }
    
    private N multiplyTrivially(D a, N b) {
        if (a.equals(getZeroDigit()))
            return getZeroNumber();
        int sig = b.significance();
        D carry = getZeroDigit();
        ArrayList<D> digits = new ArrayList<D>();
        for (int i = 0; i < sig || !carry.equals(getZeroDigit()); i++) {
            D dig = multDig(a, b.getDigit(i), carry);
            carry = multCarry(a, b.getDigit(i), carry);
            digits.add(dig);
        }
        digits.trimToSize();
        return create(digits.toArray(getEmptyArrayForCast()));
    }
    
    public String add(String a, String b) {
        return add(toNumber(a), toNumber(b)).toString();
    }
    
    public String multiply(String a, String b) {
        return multiply(toNumber(a), toNumber(b)).toString();
    }
    
    public String subtract(String a, String b) {
        return subtract(toNumber(a), toNumber(b)).toString();
    }
    
    public N toNumber(String s) {
        ArrayList<D> digits = new ArrayList<D>();
        for (char c : s.toCharArray()) {
            digits.add(0, getDigitFromChar(c));
        }
        digits.trimToSize();
        return create(digits.toArray(getEmptyArrayForCast()));
    }
    
    public N toNumber(int i) {
        boolean neg = false;
        
        if (i < 0) {
            neg = true;
            i *= -1;
        }
            
        N sum = getZeroNumber();
        for (char c : Integer.toString(i).toCharArray()) { //TODO: make this better
            sum = add(multiply(sum, getDecimalNumber(10)), 
                      getDecimalNumber(c - '0'));
        }
        
        return neg ? multiply(getNegativeOneNumber(), sum) : sum;
    }
}
