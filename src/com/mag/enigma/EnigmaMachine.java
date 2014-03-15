package com.mag.enigma;

/**
 *
 * @author michael
 */
public class EnigmaMachine {
    Rotor L, M, R;
    Reflector F;

    public EnigmaMachine(Rotor L, Rotor M, Rotor R, Reflector F) {
        this.L = L;
        this.M = M;
        this.R = R;
        this.F = F;
    }

    private char encipher(char c) { //R, M, L, F, L', M', R'.
        c = R.encipher(c);
        c = M.encipher(c);
        c = L.encipher(c);
        c = F.reflect(c);
        c = L.revcipher(c);
        c = M.revcipher(c);
        c = R.revcipher(c);
        kick();
        return c;
    }

    public void reset() {
        L.reset();
        M.reset();
        R.reset();
    }

    public String encodeText(String text) {
        StringBuilder s = new StringBuilder();

        for (char c : text.toCharArray()) {
            char cu = Character.toUpperCase(c);

            if (cu >= 'A' && cu <= 'Z') {
                s.append(encipher(cu));
            } else
                s.append(c);
        }

        return s.toString();
    }

    private void kick() {
        if (R.kick()) {
            if (M.kick()) {
                L.kick();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(R).append("\n").append(M).append("\n").append(L).append("\n").append(F).append("");
        return sb.toString();
    }
}
