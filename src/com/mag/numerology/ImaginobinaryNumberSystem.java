package com.mag.numerology;

import com.mag.numerology.ImaginobinaryNumberSystem.ImaginobinaryDigit;
import com.mag.numerology.ImaginobinaryNumberSystem.ImaginobinaryNumber;
import static com.mag.numerology.ImaginobinaryNumberSystem.ImaginobinaryDigit.*;
import com.mag.numerology.NumberSystem.Num;
import java.util.Scanner;

/**
 *
 * @author michael
 */
public class ImaginobinaryNumberSystem extends NumberSystem<ImaginobinaryNumber, ImaginobinaryDigit> {
    public static void main(String[] args) {
        ImaginobinaryNumberSystem bns = new ImaginobinaryNumberSystem();
        ImaginobinaryNumber num = bns.getZeroNumber();
        System.out.println(bns.toNumber(-36) + " + " + bns.toNumber(28) + " = " + bns.add(bns.toNumber(-36), bns.toNumber(28)) + " " + bns.add(bns.toNumber(-36), bns.toNumber(28)).toDecimalRepresentation());
    }
    
    public static class ImaginobinaryNumber extends Num<ImaginobinaryNumber, ImaginobinaryDigit> {
        public ImaginobinaryNumber(ImaginobinaryNumberSystem ns, ImaginobinaryDigit[] digits) {
            super(ns, digits);
        }
    }

    public static enum ImaginobinaryDigit {
        NEGONE_C("-1"), //(-1) for carry
        NEGI_C("-i"),   //(-i) for carry
        NEGJ_C("-j"),   //(-1-i) or (-j) for carry
        JCONJ_C("j*"),   //(1-i) or j* for carry
        ZERO("0"),
        ONE("1"),
        I("i"),
        J("j"), //(1 + i)
        NEGJCONJ_C("-j*"); //-j* or (i-1) for cary
        
        private String dig;

        private ImaginobinaryDigit(String dig) {
            this.dig = dig;
        }

        @Override
        public String toString() {
            return dig;
        }
    }
    
    public static ImaginobinaryDigit[][] decimals =
    {   {ZERO}, //0
        {ONE}, //1
        {ZERO, I, ONE, I}, //2
        {ONE, I, ONE, I}, //3
        {ZERO, ZERO, ONE, I}, //4
        {ONE, ZERO, ONE, I}, //5
        {ZERO, I, ZERO, I}, //6
        {ONE, I, ZERO, I}, //7
        {ZERO, ZERO, ZERO, I}, //8
        {ONE, ZERO, ZERO, I}, //9
        {ZERO, I, ONE, ZERO, ONE}  //10
    };

    public static final ImaginobinaryDigit[][] digs =
    {   //-2i -i   0    i  2i    3i
        {ZERO, I, ZERO, I, ZERO, I}, //-2
        {ONE,  J, ONE,  J, ONE,  J}, //-1
        {ZERO, I, ZERO, I, ZERO, I}, // 0
        {ONE,  J, ONE,  J, ONE,  J}, // 1
        {ZERO, I, ZERO, I, ZERO, I}, // 2
        {ONE,  J, ONE,  J, ONE,  J}, // 3
    };

    public static final ImaginobinaryDigit[][] carrys =
    {   //-2i        -i          0       i       2i       3i
        {NEGJCONJ_C, NEGJCONJ_C, I,      I,      J,       J      }, //-2
        {NEGJCONJ_C, NEGJCONJ_C, I,      I,      J,       J      }, //-1
        {NEGONE_C,   NEGONE_C,   ZERO,   ZERO,   ONE,     ONE    }, // 0
        {NEGONE_C,   NEGONE_C,   ZERO,   ZERO,   ONE,     ONE    }, // 1
        {NEGJ_C,     NEGJ_C,     NEGI_C, NEGI_C, JCONJ_C, JCONJ_C}, // 2
        {NEGJ_C,     NEGJ_C,     NEGI_C, NEGI_C, JCONJ_C, JCONJ_C}  // 3
    };

    @Override
    public ImaginobinaryDigit getZeroDigit() {
        return ZERO;
    }

    @Override
    public ImaginobinaryNumber getZeroNumber() {
        return create(new ImaginobinaryDigit[] {ZERO});
    }

    @Override
    public ImaginobinaryNumber getNegativeOneNumber() {
        //-1 = i1 or [1, i] in LSD representation for arrays.
        return create(new ImaginobinaryDigit[] {ONE, I});
    }

    @Override
    public ImaginobinaryNumber shiftSignificant(ImaginobinaryNumber d) {
        ImaginobinaryDigit[] arr = 
                new ImaginobinaryDigit[d.significance() + 1];
        for (int i = 0; i < d.significance(); i++)
            arr[i + 1] = d.getDigit(i);
        arr[0] = ZERO;
        return create(arr);
    }

    @Override
    public ImaginobinaryDigit addDig(ImaginobinaryDigit a, ImaginobinaryDigit b, ImaginobinaryDigit c) {
        int ap = getAPart(a) + getAPart(b) + getAPart(c);
        int bp = getBPart(a) + getBPart(b) + getBPart(c);

        ImaginobinaryDigit digit = digs[ap+2][bp+2];

        if (digit != null)
            return digit;

        System.out.printf("No digit representation for %d+%di\n", ap, bp);
        System.exit(0);
        return null;
    }

    @Override
    public ImaginobinaryDigit addCarry(ImaginobinaryDigit a, ImaginobinaryDigit b, ImaginobinaryDigit c) {
        int ap = getAPart(a) + getAPart(b) + getAPart(c);
        int bp = getBPart(a) + getBPart(b) + getBPart(c);

        ImaginobinaryDigit digit = carrys[ap+2][bp+2];

        if (digit != null)
            return digit;

        System.out.printf("No carry representation for %d+%di\n", ap, bp);
        System.exit(0);
        return null;
    }

    @Override
    public ImaginobinaryDigit multDig(ImaginobinaryDigit a, ImaginobinaryDigit b, ImaginobinaryDigit c) {
        int ap = getAPart(a) * getAPart(b) - getBPart(a) * getBPart(b)
                + getAPart(c);
        int bp = getAPart(a) * getBPart(b) + getAPart(b) * getBPart(a)
                + getBPart(c);

        ImaginobinaryDigit digit = digs[ap+2][bp+2];

        if (digit != null)
            return digit;

        System.out.printf("No digit representation for %d+%di\n", ap, bp);
        System.exit(0);
        return null;
    }

    @Override
    public ImaginobinaryDigit multCarry(ImaginobinaryDigit a, ImaginobinaryDigit b, ImaginobinaryDigit c) {
        int ap = getAPart(a) * getAPart(b) - getBPart(a) * getBPart(b)
                + getAPart(c);
        int bp = getAPart(a) * getBPart(b) + getAPart(b) * getBPart(a)
                + getBPart(c);

        ImaginobinaryDigit digit = carrys[ap+2][bp+2];

        if (digit != null)
            return digit;

        System.out.printf("No carry representation for %d+%di\n", ap, bp);
        System.exit(0);
        return null;
    }

    int getAPart(ImaginobinaryDigit i) {
        switch (i) {
            case NEGI_C:
            case ZERO:
            case I:
                return 0;
            case NEGJ_C:
            case NEGONE_C:
            case NEGJCONJ_C:
                return -1;
            case J:
            case JCONJ_C:
            case ONE:
                return 1;
        }
        return 0;
    }

    int getBPart(ImaginobinaryDigit i) {
        switch (i) {
            case NEGONE_C:
            case ZERO:
            case ONE:
                return 0;
            case NEGI_C:
            case NEGJ_C:
            case JCONJ_C:
                return -1;
            case I:
            case J:
            case NEGJCONJ_C:
                return 1;
        }
        return 0;
    }

    @Override
    public ImaginobinaryDigit[] getEmptyArrayForCast() {
        return new ImaginobinaryDigit[0];
    }

    @Override
    public ImaginobinaryNumber create(ImaginobinaryDigit[] a) {
        return new ImaginobinaryNumber(this, a);
    }

    @Override
    public ImaginobinaryDigit getDigitFromChar(char c) {
        switch (c) {
            case '0':
                return ZERO;
            case 'i':
                return I;
            case 'j':
                return J;
            case '1':
                return ONE;
        }
        throw new NumberFormatException("Unknown digit: "+c);
    }

    @Override
    public String toDecimalRepresentation(ImaginobinaryNumber n) {
        int asum = 0;
        int bsum = 0;
        int asig = 1;
        int bsig = 0;

        for (int i = 0; i < n.significance(); i++) {
            int anum = 0, bnum = 0;

            switch (n.getDigit(i)) {
                case ZERO:
                    anum = 0; bnum = 0;
                    break;
                case ONE:
                    anum = 1; bnum = 0;
                    break;
                case I:
                    anum = 0; bnum = 1;
                    break;
                case J:
                    anum = 1; bnum = 1;
                    break;
                default:
                    throw new NumberFormatException(
                            "unknown digit" + n.getDigit(i));
            }

            //(anum + bnum*i)(asig + bsig*i) -> apnum + bpnum*i
            int apnum = anum * asig - bnum * bsig;
            int bpnum = anum * bsig + bnum * asig;

            asum += apnum;
            bsum += bpnum;

            int apsig = -2 * bsig;
            int bpsig = 2 * asig;
            asig = apsig;
            bsig = bpsig;
        }

        if (asum == 0)
            return bsum == 0 ? "0" : (bsum != 1 ? (bsum + "i") : "i");

        if (bsum < 0)
            return asum + "-" + -bsum + "i";
        else if (bsum == 0)
            return asum + "";
        else //if (bsum > 0)
            return asum + "+" + (bsum != 1 ? (bsum + "i") : "i");
    }

    @Override
    public ImaginobinaryNumber getDecimalNumber(int i) {
        return create(decimals[i]);
    }
    
    public ImaginobinaryNumberSystem() {
        super();
    }
}
