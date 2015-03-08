package com.mag.enigma;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author michael
 */
public class Rotor {
    //Some of the actual enigma rotors.
    public static final Rotor ALPHA = new Rotor("EKMFLGDQVZNTOWYHXUSPAIBRCJ".toCharArray()), //not true alpha rotor**
    BETA = new Rotor("LEYJVCNIXWPBQMDRTAKZGFUHOS".toCharArray()),
    GAMMA = new Rotor("FSOKANUERHMBTIYCWLQPZXVGJD".toCharArray()),
    VI = new Rotor("JPGVOUMFYQBENHZRDKASXLICTW".toCharArray()),
    VII = new Rotor("NZJHGRCXMYSWBOUFAIVLPEKQDT".toCharArray());

    private Map<Character, Character> pristine = new HashMap<Character, Character>();
    private Map<Character, Character> forwards = new HashMap<Character, Character>(), backwards = new HashMap<Character, Character>();
    private int kick = 0;

    public Rotor(char[] translated) {
        for (int i = 0; i < translated.length; i++) {
            pristine.put((char)(i + 'A'), Character.toUpperCase(translated[i]));
            forwards.put((char)(i + 'A'), Character.toUpperCase(translated[i]));
        }

        reinitialize();
    }

    public void reset() {
        //System.out.println("Dirty: "+this);
        forwards.clear();
        forwards.putAll(pristine);
        reinitialize();
        kick = 0;
        //System.out.println("Clean: "+this);
    }

    public boolean kick() {
        HashMap<Character, Character> reint = new HashMap<Character, Character>();
        char aGets = forwards.get('A');

        for (char i = 'A'; i < 'Z'; i++) //iterate [A, Z)
            reint.put(i, forwards.get((char)(i + 1)));

        reint.put('Z', aGets);
        assert reint.size() == 26;
        forwards = reint;
        reinitialize();
        kick++;

        if (kick == 26) {
            kick = 0;
            return true;
        }

        return false;
    }

    public char encipher(char c) {
        c = Character.toUpperCase(c);
        assert c >= 'A' && c <= 'Z';
        return forwards.get(c);
    }

    public char revcipher(char c) {
        c = Character.toUpperCase(c);
        assert c >= 'A' && c <= 'Z';
        return backwards.get(c);
    }

    private void reinitialize() {
        backwards.clear();

        for (Entry<Character, Character> e : forwards.entrySet())
            backwards.put(e.getValue(), e.getKey());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (Entry<Character, Character> e : forwards.entrySet())
            sb.append(e.getKey()).append("->").append(e.getValue()).append(" ");

        sb.append("Rotate #: ").append(kick);
        return sb.toString();
    }
}
