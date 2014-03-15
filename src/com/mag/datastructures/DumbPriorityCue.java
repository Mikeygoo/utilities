/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mag.datastructures;

import com.mag.utils.Pair;
import java.util.Comparator;

/**
 *
 * @author admin
 */
public class DumbPriorityCue<T> {
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_NORMAL = 2;
    public static final int PRIORITY_HIGH = 3;

    //comparator that relates elements based on the first element of the pair, {Integer} priority.
    Comparator<Pair<Integer, T>> comp = new Comparator<Pair<Integer, T>>() {
        @Override
        public int compare(Pair<Integer, T> o1, Pair<Integer, T> o2) {
            return o2.getFirst().compareTo(o1.getFirst());
        }
    };

    //underlying queue, does all of the heavy lifting through the comparator and an underlying data structure {LinkedList}.
    PriorityCue<Pair<Integer, T>> pq = new PriorityCue<Pair<Integer, T>>(comp);

    public boolean enqueue(T t, int priority) {
        pq.add(new Pair<Integer, T>(priority, t));
        return true;
    }

    public boolean enqueue(T t) {
        return enqueue(t, PRIORITY_NORMAL);
    }

    public T dequeue() {
        if (count() == 0)
            return null;

        return pq.poll().getSecond();
    }

    public T peek() {
        if (count() == 0)
            return null;

        return pq.peek().getSecond();
    }

    public int count() {
        return pq.size();
    }
}
