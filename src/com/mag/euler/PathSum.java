package com.mag.euler;

import java.util.PriorityQueue;
import java.util.Scanner;


/**
 * Used for Euler 81, 82, 83
 * @author michael
 */
public class PathSum {
    public static int SIZE = 80;
    public static int[][] array = new int[SIZE][SIZE];
    public static int[][] evaluated = new int[SIZE][SIZE];
    public static int smallest_path_definite = Integer.MAX_VALUE;
    
    public static void main(String[] args) {
        
        Scanner s = new Scanner(System.in);
        s.useDelimiter("[, \n]");
        
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                array[i][j] = s.nextInt();
            }
            s.nextLine();
        }
        
        for (int i1 = 0; i1 < SIZE; i1++)
            for (int i2 = 0; i2 < SIZE; i2++) {
                System.out.println("did " + i1 + ", " + i2);
                clearEvaluated();
                smallest_path_definite = Math.min(smallest_path_definite, findSmallestPath(i1, 0, i2, SIZE-1));
            }
        
        System.out.println(smallest_path_definite);
    }
    
    private static PriorityQueue<Node> q = new PriorityQueue<>();

    private static int findSmallestPath(int I1, int J1, int I2, int J2) {
        q.add(new Node(I1, J1, array[I1][J1]));
        
        while (!q.isEmpty()) {
            Node current = q.poll();
            
            if (current.i == I2 && current.j == J2) {
                return current.distance;
            }
            
            evaluated[current.i][current.j] = current.distance;
            
            if (current.distance > smallest_path_definite)
                continue;
            
            add(current.up());
            add(current.down());
            //add(current.left());
            add(current.right());
        }
        
        return Integer.MAX_VALUE;
    }

    private static void add(Node n) {
        if (n == null)
            return;
        
        if (evaluated[n.i][n.j] != 0 && evaluated[n.i][n.j] <= n.distance)
            return;
        
        q.add(n);
    }

    private static void clearEvaluated() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                evaluated[i][j] = 0;
    }
    
    private static class Node implements Comparable<Node> {
        int i, j;
        int distance;

        private Node(int i, int j, int d) {
            this.i = i;
            this.j = j;
            distance = d;
        }

        @Override
        public int compareTo(Node o) {
            return distance - o.distance;
        }

        private Node up() {
            if (i - 1 >= 0)
                return new Node(i - 1, j, distance + array[i - 1][j]);
            return null;
        }

        private Node down() {
            if (i + 1 < SIZE)
                return new Node(i + 1, j, distance + array[i + 1][j]);
            return null;
        }

        private Node left() {
            if (j - 1 >= 0)
                return new Node(i, j - 1, distance + array[i][j - 1]);
            return null;
        }

        private Node right() {
            if (j + 1 < SIZE)
                return new Node(i, j + 1, distance + array[i][j + 1]);
            return null;
        }

        @Override
        public String toString() {
            return distance + "";
        }
    }
}
