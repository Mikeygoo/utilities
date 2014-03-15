package com.mag.enigma;

/**
 *
 * @author michael
 */
public class Reflector {
    public static final Reflector A = new Reflector("AB CD EF GH IJ KL MN OP QR ST UV WX YZ"), //Not true A-Reflector**
    B = new Reflector("AY BR CU DH EQ FS GL IP JX KN MO TZ VW"),
    C = new Reflector("AF BV CP DJ EI GO HY KR LZ MX NW QT SU"),
    LIGHT_B = new Reflector("AE BN CK DQ FU GY HW IJ LO MP RX SZ TV");

    private char[] reflected;

    public Reflector(String spaceDelimitedPairs) {
        String[] pairs = spaceDelimitedPairs.toUpperCase().split(" ");
        assert pairs.length == 13;

        reflected = new char[26];

        for (String pair : pairs) {
            char a = pair.charAt(0);
            char b = pair.charAt(1);
            reflected[a - 'A'] = b;
            reflected[b - 'A'] = a;
        }
    }

    public char reflect(char c) {
        assert(c >= 'A' && c <= 'Z');
        return reflected[c - 'A'];
    }

    @Override
    public String toString() {
        String s = "{";

        for (char c = 'A'; c <= 'Z'; c++)
            s += c + "->" + reflect(c) + " ";

        return s + "\b}";
    }
}
