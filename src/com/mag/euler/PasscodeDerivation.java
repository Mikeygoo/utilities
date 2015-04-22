package com.mag.euler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author michael
 */
public class PasscodeDerivation {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        
        Node base = new Node("");
        
        for (int i = 0; i < 50; i++) {
            String attempt = s.nextLine();
            String a = attempt.charAt(0) + "";
            String b = attempt.charAt(1) + "";
            String c = attempt.charAt(2) + "";
            base.hasChildOtherwiseAdd(a).hasChildOtherwiseAdd(b).hasChildOtherwiseAdd(c);
            base.cull();
        }
        
        base.print();
    }
    
    private static class Node {
        String name = "";
        ArrayList<Node> children = new ArrayList<>();

        private Node(String string) {
            name = string;
        }
        
        private void cull() {
            Node child;
            outer: for (Iterator<Node> i = children.iterator(); i.hasNext();) {
                child = i.next();
                
                for (Node otherChild : children) {
                    if (otherChild == child)
                        continue;
                    
                    if (otherChild.hasChild(child.name) != null) {
                        i.remove();
                        continue outer;
                    }
                }
            }
            
            for (Node c : children)
                c.cull();
        }

        private Node hasChild(String child) {
            if (child.equals(name))
                return this;
            
            for (Node c : children) {
                Node success = c.hasChild(child);
                if (success != null)
                    return success;
            }
            
            return null;
        }
        
        private Node hasChildOtherwiseAdd(String child) {
            Node possible = hasChild(child);
            
            if (possible != null)
                return possible;
            
            Node n = new Node(child);
            children.add(n);
            return n;
        }

        private void print() {
            System.out.print(name);
            if (!children.isEmpty())
                children.get(0).print();
        }
    }
}
