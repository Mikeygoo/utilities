package com.mag.numerology;

import com.mag.numerology.BalpentaryNumberSystem.BalpentaryDigit;
import static com.mag.numerology.BalpentaryNumberSystem.BalpentaryDigit.*;
import com.mag.numerology.BalpentaryNumberSystem.BalpentaryNumber;
import com.mag.numerology.NumberSystem.Num;
import java.util.Scanner;

/**
 *
 * @author michael
 */
public class BalpentaryNumberSystem extends NumberSystem<BalpentaryNumber, BalpentaryDigit> {
    public static void main(String[] args) {
        BalpentaryNumberSystem bns = new BalpentaryNumberSystem();
        Scanner s = new Scanner(System.in);
        String k = "1";
        while (true) {
            k = bns.multiply(k, "T21");
            System.out.println(k + " " + bns.toNumber(k).toDecimalRepresentation());
        }
    }
    
    public static class BalpentaryNumber extends Num<BalpentaryNumber, BalpentaryDigit> {
        public BalpentaryNumber(BalpentaryNumberSystem ns, BalpentaryDigit[] digits) {
            super(ns, digits);
        }
    }

    public static enum BalpentaryDigit {
        U("U"), //-2
        T("T"), //-1
        ZERO("0"),
        ONE("1"),
        TWO("2");
        
        private String dig;

        private BalpentaryDigit(String dig) {
            this.dig = dig;
        }

        @Override
        public String toString() {
            return dig;
        }
    }
    
    public static BalpentaryDigit[][] decimals =
    {   {ZERO}, //0
        {ONE}, //1
        {TWO}, //2
        {U, ONE}, //3
        {T, ONE}, //4
        {ZERO, ONE}, //5
        {ONE, ONE}, //6
        {TWO, ONE}, //7
        {U, TWO}, //8
        {T, TWO}, //9
        {ZERO, TWO} //10
    };

    @Override
    public BalpentaryDigit getZeroDigit() {
        return ZERO;
    }

    @Override
    public BalpentaryNumber getZeroNumber() {
        return create(new BalpentaryDigit[] {ZERO});
    }

    @Override
    public BalpentaryNumber getNegativeOneNumber() {
        //-1 = i1 or [1, i] in LSD representation for arrays.
        return create(new BalpentaryDigit[] {T});
    }

    @Override
    public BalpentaryNumber shiftSignificant(BalpentaryNumber d) {
        BalpentaryDigit[] arr = new BalpentaryDigit[d.significance() + 1];
        for (int i = 0; i < d.significance(); i++)
            arr[i + 1] = d.getDigit(i);
        arr[0] = ZERO;
        return create(arr);
    }

    @Override
    public BalpentaryDigit addDig(BalpentaryDigit a, BalpentaryDigit b, BalpentaryDigit c) {
        int sum = 0;

        switch (a) {
            case U:
                sum += -2;
                break;
            case T:
                sum += -1;
                break;
            case ZERO:
                sum += 0;
                break;
            case ONE:
                sum += 1;
                break;
            case TWO:
                sum += 2;
                break;
        }
        
        switch (b) {
            case U:
                sum += -2;
                break;
            case T:
                sum += -1;
                break;
            case ZERO:
                sum += 0;
                break;
            case ONE:
                sum += 1;
                break;
            case TWO:
                sum += 2;
                break;
        }
        
        switch (c) {
            case U:
                sum += -2;
                break;
            case T:
                sum += -1;
                break;
            case ZERO:
                sum += 0;
                break;
            case ONE:
                sum += 1;
                break;
            case TWO:
                sum += 2;
                break;
        }
        
        switch (sum) {
            case -2:
            case 3:
                return U;
            case -1:
            case 4:
                return T;
            case -5:
            case 0:
            case 5:
                return ZERO;
            case -4:
            case 1:
                return ONE;
            case -3:
            case 2:
                return TWO;
        }

        System.out.println("No digit representation for "+sum);
        System.exit(0);
        return null;
    }

    @Override
    public BalpentaryDigit addCarry(BalpentaryDigit a, BalpentaryDigit b, BalpentaryDigit c) {
        int sum = 0;

        switch (a) {
            case U:
                sum += -2;
                break;
            case T:
                sum += -1;
                break;
            case ZERO:
                sum += 0;
                break;
            case ONE:
                sum += 1;
                break;
            case TWO:
                sum += 2;
                break;
        }
        
        switch (b) {
            case U:
                sum += -2;
                break;
            case T:
                sum += -1;
                break;
            case ZERO:
                sum += 0;
                break;
            case ONE:
                sum += 1;
                break;
            case TWO:
                sum += 2;
                break;
        }
        
        switch (c) {
            case U:
                sum += -2;
                break;
            case T:
                sum += -1;
                break;
            case ZERO:
                sum += 0;
                break;
            case ONE:
                sum += 1;
                break;
            case TWO:
                sum += 2;
                break;
        }
        
        switch (sum) {
            case -5:
            case -4:
            case -3:
                return T;
            case -2:
            case -1:
            case 0:
            case 1:
            case 2:
                return ZERO;
            case 3:
            case 4:
            case 5:
                return ONE;
        }

        System.out.println("No carry representation for "+sum);
        System.exit(0);
        return null;
    }

    @Override
    public BalpentaryDigit multDig(BalpentaryDigit a, BalpentaryDigit b, BalpentaryDigit c) {
        int sum = 1;

        switch (a) {
            case U:
                sum *= -2;
                break;
            case T:
                sum *= -1;
                break;
            case ZERO:
                sum *= 0;
                break;
            case ONE:
                sum *= 1;
                break;
            case TWO:
                sum *= 2;
                break;
        }
        
        switch (b) {
            case U:
                sum *= -2;
                break;
            case T:
                sum *= -1;
                break;
            case ZERO:
                sum *= 0;
                break;
            case ONE:
                sum *= 1;
                break;
            case TWO:
                sum *= 2;
                break;
        }
        
        switch (c) {
            case U:
                sum += -2;
                break;
            case T:
                sum += -1;
                break;
            case ZERO:
                sum += 0;
                break;
            case ONE:
                sum += 1;
                break;
            case TWO:
                sum += 2;
                break;
        }
        
        switch (sum) {
            case -2:
            case 3:
                return U;
            case -1:
            case 4:
                return T;
            case -5:
            case 0:
            case 5:
                return ZERO;
            case -4:
            case 1:
                return ONE;
            case -3:
            case 2:
                return TWO;
        }

        System.out.println("No carry representation for "+sum);
        System.exit(0);
        return null;
    }

    @Override
    public BalpentaryDigit multCarry(BalpentaryDigit a, BalpentaryDigit b, BalpentaryDigit c) {
        int sum = 1;

        switch (a) {
            case U:
                sum *= -2;
                break;
            case T:
                sum *= -1;
                break;
            case ZERO:
                sum *= 0;
                break;
            case ONE:
                sum *= 1;
                break;
            case TWO:
                sum *= 2;
                break;
        }
        
        switch (b) {
            case U:
                sum *= -2;
                break;
            case T:
                sum *= -1;
                break;
            case ZERO:
                sum *= 0;
                break;
            case ONE:
                sum *= 1;
                break;
            case TWO:
                sum *= 2;
                break;
        }
        
        switch (c) {
            case U:
                sum += -2;
                break;
            case T:
                sum += -1;
                break;
            case ZERO:
                sum += 0;
                break;
            case ONE:
                sum += 1;
                break;
            case TWO:
                sum += 2;
                break;
        }
        
        switch (sum) {
            case -5:
            case -4:
            case -3:
                return T;
            case -2:
            case -1:
            case 0:
            case 1:
            case 2:
                return ZERO;
            case 3:
            case 4:
            case 5:
                return ONE;
        }

        System.out.println("No carry representation for "+sum);
        System.exit(0);
        return null;
    }

    @Override
    public BalpentaryDigit[] getEmptyArrayForCast() {
        return new BalpentaryDigit[0];
    }

    @Override
    public BalpentaryNumber create(BalpentaryDigit[] a) {
        return new BalpentaryNumber(this, a);
    }

    @Override
    public BalpentaryDigit getDigitFromChar(char c) {
        switch (c) {
            case '0':
                return ZERO;
            case 'U':
                return U;
            case 'T':
                return T;
            case '1':
                return ONE;
            case '2':
                return TWO;
        }
        throw new NumberFormatException("Unknown digit: "+c);
    }

    @Override
    public String toDecimalRepresentation(BalpentaryNumber n) {
        int sum = 0;
        int sig = 1;

        for (int i = 0; i < n.significance(); i++) {
            switch (n.getDigit(i)) {
                case ONE:
                    sum += sig;
                    break;
                case TWO:
                    sum += (2 * sig);
                    break;
                case T:
                    sum -= sig;
                    break;
                case U:
                    sum -= (2 * sig);
                    break;
            }
            sig *= 5;
        }

        return "" + sum;
    }

    @Override
    public BalpentaryNumber getDecimalNumber(int i) {
        return create(decimals[i]);
    }
    
    public BalpentaryNumberSystem() {
        super();
    }
}
