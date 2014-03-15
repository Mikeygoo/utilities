package com.mag.enigma;

import java.util.Scanner;

/**
 *
 * @author michael
 */
public class RotorTest {
    public static void main(String[] args) {
        Rotor left = Rotor.ALPHA, middle = Rotor.BETA, right = Rotor.VI;
        Reflector reflector = Reflector.LIGHT_B;
        EnigmaMachine em = new EnigmaMachine(left, middle, right, reflector);

        Scanner s = new Scanner(System.in);

        while (true) {
            System.out.print("Provide a string: ");
            String line = s.nextLine();
            System.out.println("Encoded: " + em.encodeText(line) + "\n");
            System.out.println(em);
            em.reset();
        }
    }
}
