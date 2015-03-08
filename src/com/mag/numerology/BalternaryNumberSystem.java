package com.mag.numerology;

import com.mag.numerology.BalternaryNumberSystem.BalternaryDigit;
import static com.mag.numerology.BalternaryNumberSystem.BalternaryDigit.*;
import com.mag.numerology.BalternaryNumberSystem.BalternaryNumber;
import com.mag.numerology.NumberSystem.Num;
import java.util.Scanner;

/**
 *
 * @author michael
 */
public class BalternaryNumberSystem extends NumberSystem<BalternaryNumber, BalternaryDigit> {
    public static void main(String[] args) {
        BalternaryNumberSystem bns = new BalternaryNumberSystem();
        Scanner s = new Scanner(System.in);
        System.out.println(bns.toNumber(-36_438_111) + " + " + bns.toNumber(28_345_112) + " = " + bns.add(bns.toNumber(-36_438_111), bns.toNumber(28_345_112)) + " " + bns.add(bns.toNumber(-36_438_111), bns.toNumber(28_345_112)).toDecimalRepresentation());
    }
    
    public static class BalternaryNumber extends Num<BalternaryNumber, BalternaryDigit> {
        public BalternaryNumber(BalternaryNumberSystem ns, BalternaryDigit[] digits) {
            super(ns, digits);
        }
    }

    public static enum BalternaryDigit {
        T("T"),
        ZERO("0"),
        ONE("1");
        
        private String dig;

        private BalternaryDigit(String dig) {
            this.dig = dig;
        }

        @Override
        public String toString() {
            return dig;
        }
    }
    
    public static BalternaryDigit[][] decimals =
    {   {ZERO}, //0 = 0_t
        {ONE}, //1 = 1_t
        {T, ONE}, //2 = 1T_t
        {ZERO, ONE}, //3 = 10_t
        {ONE, ONE}, //4 = 11_t
        {T, T, ONE}, //5 = 1TT_t
        {ZERO, T, ONE}, //6 = 1T0_t
        {ONE, T, ONE}, //7 = 1T1_t
        {T, ZERO, ONE}, //8 = 10T_t
        {ZERO, ZERO, ONE},  //9 = 100_t
        {ONE, ZERO, ONE}  //10 = 101_t
    };

    @Override
    public BalternaryDigit getZeroDigit() {
        return ZERO;
    }

    @Override
    public BalternaryNumber getZeroNumber() {
        return create(new BalternaryDigit[] {ZERO});
    }

    @Override
    public BalternaryNumber getNegativeOneNumber() {
        //-1 = i1 or [1, i] in LSD representation for arrays.
        return create(new BalternaryDigit[] {T});
    }

    @Override
    public BalternaryNumber shiftSignificant(BalternaryNumber d) {
        BalternaryDigit[] arr = new BalternaryDigit[d.significance() + 1];
        for (int i = 0; i < d.significance(); i++)
            arr[i + 1] = d.getDigit(i);
        arr[0] = ZERO;
        return create(arr);
    }

    @Override
    public BalternaryDigit addDig(BalternaryDigit a, BalternaryDigit b, BalternaryDigit c) {
        int sum = 0;

        if (a == ONE)
            sum++;
        else if (a == T)
            sum--;

        if (b == ONE)
            sum++;
        else if (b == T)
            sum--;

        if (c == ONE)
            sum++;
        else if (c == T)
            sum--;

        if (sum == -3 || sum == 0 || sum == 3)
            return ZERO;
        if (sum == -2 || sum == 1)
            return ONE;
        if (sum == -1 || sum == 2)
            return T;

        System.out.println("No carry representation for "+sum);
        System.exit(0);
        return null;
    }

    @Override
    public BalternaryDigit addCarry(BalternaryDigit a, BalternaryDigit b, BalternaryDigit c) {
        int sum = 0;

        if (a == ONE)
            sum++;
        else if (a == T)
            sum--;

        if (b == ONE)
            sum++;
        else if (b == T)
            sum--;

        if (c == ONE)
            sum++;
        else if (c == T)
            sum--;

        if (sum == -3 || sum == -2)
            return T;
        if (sum == -1 || sum == 0 || sum == 1)
            return ZERO;
        if (sum == 2 || sum == 3)
            return ONE;

        System.out.println("No carry representation for "+sum);
        System.exit(0);
        return null;
    }

    @Override
    public BalternaryDigit multDig(BalternaryDigit a, BalternaryDigit b, BalternaryDigit c) {
        int sum = 1;

        if (a == ZERO)
            sum *= 0;
        else if (a == T)
            sum *= -1;

        if (b == ZERO)
            sum *= 0;
        else if (b == T)
            sum *= -1;

        if (c == ONE)
            sum++;
        else if (c == T)
            sum--;

        if (sum == -3 || sum == 0 || sum == 3)
            return ZERO;
        if (sum == -2 || sum == 1)
            return ONE;
        if (sum == -1 || sum == 2)
            return T;

        System.out.println("No carry representation for "+sum);
        System.exit(0);
        return null;
    }

    @Override
    public BalternaryDigit multCarry(BalternaryDigit a, BalternaryDigit b, BalternaryDigit c) {
        int sum = 1;

        if (a == ZERO)
            sum *= 0;
        else if (a == T)
            sum *= -1;

        if (b == ZERO)
            sum *= 0;
        else if (b == T)
            sum *= -1;

        if (c == ONE)
            sum++;
        else if (c == T)
            sum--;

        if (sum == -3 || sum == -2)
            return T;
        if (sum == -1 || sum == 0 || sum == 1)
            return ZERO;
        if (sum == 2 || sum == 2)
            return ONE;

        System.out.println("No carry representation for "+sum);
        System.exit(0);
        return null;
    }

    @Override
    public BalternaryDigit[] getEmptyArrayForCast() {
        return new BalternaryDigit[0];
    }

    @Override
    public BalternaryNumber create(BalternaryDigit[] a) {
        return new BalternaryNumber(this, a);
    }

    @Override
    public BalternaryDigit getDigitFromChar(char c) {
        switch (c) {
            case '0':
                return ZERO;
            case 'T':
                return T;
            case '1':
                return ONE;
        }
        throw new NumberFormatException("Unknown digit: "+c);
    }

    @Override
    public String toDecimalRepresentation(BalternaryNumber n) {
        int sum = 0;
        int sig = 1;

        for (int i = 0; i < n.significance(); i++) {
            switch (n.getDigit(i)) {
                case ONE:
                    sum += sig;
                    break;
                case T:
                    sum -= sig;
                    break;
            }
            sig *= 3;
        }

        return "" + sum;
    }

    @Override
    public BalternaryNumber getDecimalNumber(int i) {
        return create(decimals[i]);
    }
    
    public BalternaryNumberSystem() {
        super();
    }
}
