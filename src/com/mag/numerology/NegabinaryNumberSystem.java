package com.mag.numerology;

import com.mag.numerology.NegabinaryNumberSystem.NegabinaryDigit;
import com.mag.numerology.NegabinaryNumberSystem.NegabinaryNumber;
import static com.mag.numerology.NegabinaryNumberSystem.NegabinaryDigit.*;
import com.mag.numerology.NumberSystem.Num;
import java.util.Scanner;

/**
 *
 * @author michael
 */
public class NegabinaryNumberSystem extends NumberSystem<NegabinaryNumber, NegabinaryDigit> {
    public static void main(String[] args) {
        NegabinaryNumberSystem bns = new NegabinaryNumberSystem();
        Scanner s = new Scanner(System.in);
        System.out.println(bns.toNumber("0110").toDecimalRepresentation());
    }
    
    public static class NegabinaryNumber extends Num<NegabinaryNumber, NegabinaryDigit> {
        public NegabinaryNumber(NegabinaryNumberSystem ds, NegabinaryDigit[] digits) {
            super(ds, digits);
        }
    }

    public static enum NegabinaryDigit {
        NEGONE_C("-1"), //(-1) for carry
        ZERO("0"),
        ONE("1");
        
        private String dig;

        private NegabinaryDigit(String dig) {
            this.dig = dig;
        }

        @Override
        public String toString() {
            return dig;
        }
    }
    
    public static NegabinaryDigit[][] decimals =
    {   {ZERO}, //0 = 0n
        {ONE}, //1 = 1n
        {ZERO, ONE, ONE}, //2 = 110n
        {ONE, ONE, ONE}, //3 = 111n
        {ZERO, ZERO, ONE}, //4 = 100n
        {ONE, ZERO, ONE}, //5 = 101n
        {ZERO, ONE, ZERO, ONE, ONE}, //6 = 11010n
        {ONE, ONE, ZERO, ONE, ONE}, //7 = 11011n
        {ZERO, ZERO, ZERO, ONE, ONE}, //8 = 11000n
        {ONE, ZERO, ZERO, ONE, ONE},  //9 = 11001n
        {ZERO, ONE, ONE, ONE, ONE}  //10 = 11110n
    };

    @Override
    public NegabinaryDigit getZeroDigit() {
        return ZERO;
    }

    @Override
    public NegabinaryNumber getZeroNumber() {
        return create(new NegabinaryDigit[] {ZERO});
    }

    @Override
    public NegabinaryNumber getNegativeOneNumber() {
        return create(new NegabinaryDigit[] {ONE, ONE});
    }

    @Override
    public NegabinaryNumber shiftSignificant(NegabinaryNumber d) {
        NegabinaryDigit[] arr = new NegabinaryDigit[d.significance() + 1];
        for (int i = 0; i < d.significance(); i++)
            arr[i + 1] = d.getDigit(i);
        arr[0] = ZERO;
        return create(arr);
    }

    @Override
    public NegabinaryDigit addDig(NegabinaryDigit a, NegabinaryDigit b, NegabinaryDigit c) {
        int i = 0;

        if (a == ONE)
            i++;
        else if (a == NEGONE_C)
            i--;

        if (b == ONE)
            i++;
        else if (b == NEGONE_C)
            i--;

        if (c == ONE)
            i++;
        else if (c == NEGONE_C)
            i--;

        if (i == -1)
            return ONE;
        else if (i == 0)
            return ZERO;
        else if (i == 1)
            return ONE;
        else if (i == 2)
            return ZERO;
        else //if (i == 3)
            return ONE;
    }

    @Override
    public NegabinaryDigit addCarry(NegabinaryDigit a, NegabinaryDigit b, NegabinaryDigit c) {
        int i = 0;

        if (a == ONE)
            i++;
        else if (a == NEGONE_C)
            i--;

        if (b == ONE)
            i++;
        else if (b == NEGONE_C)
            i--;

        if (c == ONE)
            i++;
        else if (c == NEGONE_C)
            i--;

        if (i == -1)
            return ONE;
        else if (i == 0)
            return ZERO;
        else if (i == 1)
            return ZERO;
        else if (i == 2)
            return NEGONE_C;
        else //if (i == 3)
            return NEGONE_C;
    }

    @Override
    public NegabinaryDigit multDig(NegabinaryDigit a, NegabinaryDigit b, NegabinaryDigit c) {
        int i = 1;

        if (a == ZERO)
            i *= 0;
        else if (a == NEGONE_C)
            i *= -1;

        if (b == ZERO)
            i *= 0;
        else if (b == NEGONE_C)
            i *= -1;

        if (c == ONE)
            i++;
        else if (c == NEGONE_C)
            i--;

        if (i == -1)
            return ONE;
        else if (i == 0)
            return ZERO;
        else if (i == 1)
            return ONE;
        else if (i == 2)
            return ZERO;
        else //if (i == 3)
            return ONE;
    }

    @Override
    public NegabinaryDigit multCarry(NegabinaryDigit a, NegabinaryDigit b, NegabinaryDigit c) {
        int i = 1;

        if (a == ZERO)
            i *= 0;
        else if (a == NEGONE_C)
            i *= -1;

        if (b == ZERO)
            i *= 0;
        else if (b == NEGONE_C)
            i *= -1;

        if (c == ONE)
            i++;
        else if (c == NEGONE_C)
            i--;

        if (i == -1)
            return ONE;
        else if (i == 0)
            return ZERO;
        else if (i == 1)
            return ZERO;
        else if (i == 2)
            return NEGONE_C;
        else //if (i == 3)
            return NEGONE_C;
    }

    @Override
    public NegabinaryDigit[] getEmptyArrayForCast() {
        return new NegabinaryDigit[0];
    }

    @Override
    public NegabinaryNumber create(NegabinaryDigit[] a) {
        return new NegabinaryNumber(this, a);
    }

    @Override
    public NegabinaryDigit getDigitFromChar(char c) {
        if (c == '0')
            return ZERO;
        if (c == '1')
            return ONE;
        throw new NumberFormatException("Unknown digit: "+c);
    }

    @Override
    public String toDecimalRepresentation(NegabinaryNumber n) {
        int sum = 0;
        int sig = 1;
        for (int i = 0; i < n.significance(); i++) {
            sum += (((n.getDigit(i) == ONE) ? 1 : 0) * sig);
            sig *= -2;
        }
        return "" + sum;
    }

    @Override
    public NegabinaryNumber getDecimalNumber(int i) {
        return create(decimals[i]);
    }
    
    public NegabinaryNumberSystem() {
        super();
    }
}
