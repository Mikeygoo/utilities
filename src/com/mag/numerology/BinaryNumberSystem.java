package com.mag.numerology;

import com.mag.numerology.BinaryNumberSystem.BinaryDigit;
import com.mag.numerology.BinaryNumberSystem.BinaryNumber;
import static com.mag.numerology.BinaryNumberSystem.BinaryDigit.*;
import com.mag.numerology.NumberSystem.Num;
import java.util.Scanner;

/**
 *
 * @author michael
 */
public class BinaryNumberSystem extends NumberSystem<BinaryNumber, BinaryDigit> {
    public static void main(String[] args) {
        BinaryNumberSystem bns = new BinaryNumberSystem();
        Scanner s = new Scanner(System.in);
        System.out.println(bns.toNumber(26_248_000));
    }
    
    public static class BinaryNumber extends Num<BinaryNumber, BinaryDigit> {
        public BinaryNumber(BinaryNumberSystem ns, BinaryDigit[] digits) {
            super(ns, digits);
        }
    }

    public static enum BinaryDigit {
        ZERO("0"),
        ONE("1");
        
        private String dig;

        private BinaryDigit(String dig) {
            this.dig = dig;
        }

        @Override
        public String toString() {
            return dig;
        }
    }
    
    public static BinaryDigit[][] decimals =
    {   {ZERO}, //0
        {ONE}, //1
        {ZERO, ONE}, //2
        {ONE, ONE}, //3
        {ZERO, ZERO, ONE}, //4
        {ONE, ZERO, ONE}, //5
        {ZERO, ONE, ONE}, //6
        {ONE, ONE, ONE}, //7
        {ZERO, ZERO, ZERO, ONE}, //8
        {ONE, ZERO, ZERO, ONE},  //9
        {ZERO, ONE, ZERO, ONE}  //10
    };

    @Override
    public BinaryDigit getZeroDigit() {
        return ZERO;
    }

    @Override
    public BinaryNumber getZeroNumber() {
        return create(new BinaryDigit[] {ZERO});
    }

    @Override
    public BinaryNumber getNegativeOneNumber() {
        throw new UnsupportedOperationException(
                "Binary numbers cannot be subtracted.");
    };

    @Override
    public BinaryNumber shiftSignificant(BinaryNumber d) {
        BinaryDigit[] arr = new BinaryDigit[d.significance() + 1];
        for (int i = 0; i < d.significance(); i++)
            arr[i + 1] = d.getDigit(i);
        arr[0] = ZERO;
        return create(arr);
    }

    @Override
    public BinaryDigit addDig(BinaryDigit a, BinaryDigit b, BinaryDigit c) {
        int i = 0;

        if (a == ONE)
            i++;
        if (b == ONE)
            i++;
        if (c == ONE)
            i++;

        if (i == 0 || i == 2)
            return ZERO;
        return ONE;
    }

    @Override
    public BinaryDigit addCarry(BinaryDigit a, BinaryDigit b, BinaryDigit c) {
        int i = 0;

        if (a == ONE)
            i++;
        if (b == ONE)
            i++;
        if (c == ONE)
            i++;

        if (i == 0 || i == 1)
            return ZERO;
        return ONE;
    }

    @Override
    public BinaryDigit multDig(BinaryDigit a, BinaryDigit b, BinaryDigit c) {
        int i = 0;

        if (a != ZERO && b != ZERO)
            i++;

        if (c == ONE)
            i++;

        if (i == 0 || i == 2)
            return ZERO;
        return ONE;
    }

    @Override
    public BinaryDigit multCarry(BinaryDigit a, BinaryDigit b, BinaryDigit c) {
        int i = 0;

        if (a != ZERO && b != ZERO)
            i++;

        if (c == ONE)
            i++;

        if (i == 0 || i == 1)
            return ZERO;
        return ONE;
    }

    @Override
    public BinaryDigit[] getEmptyArrayForCast() {
        return new BinaryDigit[0];
    }

    @Override
    public BinaryNumber create(BinaryDigit[] a) {
        return new BinaryNumber(this, a);
    }

    @Override
    public BinaryDigit getDigitFromChar(char c) {
        if (c == '0')
            return ZERO;
        if (c == '1')
            return ONE;
        throw new NumberFormatException("Unknown digit: "+c);
    }

    @Override
    public String toDecimalRepresentation(BinaryNumber n) {
        int sum = 0;
        int sig = 1;
        for (int i = 0; i < n.significance(); i++) {
            sum += (((n.getDigit(i) == ONE) ? 1 : 0) * sig);
            sig *= 2;
        }
        return "" + sum;
    }

    @Override
    public BinaryNumber getDecimalNumber(int i) {
        return create(decimals[i]);
    }
    
    public BinaryNumberSystem() {
        super();
    }
}
