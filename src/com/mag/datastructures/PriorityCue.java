package com.mag.datastructures;

import com.mag.utils.Pair;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

/**
 *
 * @author admin
 */
public class PriorityCue<T> extends java.util.AbstractQueue<T> {
    private Comparator<T> comp;
    private LinkedList<T> underlaying = new LinkedList<T>();

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int i = 0;
        PriorityCue<Pair<Double, Integer>> pc = new PriorityCue<Pair<Double, Integer>>(new Comparator<Pair<Double, Integer>>() {
            @Override
            public int compare(Pair<Double, Integer> o1, Pair<Double, Integer> o2) {
                return o1.getFirst().compareTo(o2.getFirst());
            }
        });

        while (true) {
            String ln = s.next();

            if (ln.matches("[0-9]*(\\.[0-9]+)?")) {
                pc.offer(new Pair<Double, Integer>(Double.parseDouble(ln), i++));
                System.out.println("Queue: " + pc);
            } else {
                System.out.println(pc.poll());
            }
        }
    }

    public PriorityCue() {
        comp = new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                if (!(o1 instanceof Comparable))
                    return Integer.MAX_VALUE;

                Comparable c1 = (Comparable) o1, c2 = (Comparable) o2;
                return c1.compareTo(o2);
            }
        };
    }

    public PriorityCue(Comparator<T> comp) {
        this.comp = comp;
    }

    @Override
    public ListIterator<T> iterator() {
        return underlaying.listIterator();
    }

    @Override
    public int size() {
        return underlaying.size();
    }

    @Override
    public boolean offer(T e) {
        for (ListIterator<T> it = iterator(); it.hasNext();) {
            T next = it.next();

            if (comp.compare(next, e) >= 1) {
                it.previous();
                it.add(e);
                return true;
            }
        }

        underlaying.add(e);
        return true;
    }

    @Override
    public T poll() {
        if (isEmpty())
            return null;

        return underlaying.remove(0);
    }

    @Override
    public T peek() {
        if (isEmpty())
            return null;

        return underlaying.get(0);
    }

    @Override
    public String toString() {
        return underlaying.toString();
    }
}
