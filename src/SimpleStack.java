/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author michael
 */
public class SimpleStack<T> {
    public static void main(String[] args) {
        SimpleStack<Integer> ss = new SimpleStack<>();
        ss.add(1);
        ss.add(2);
        
        System.out.println(ss.remove());
        System.out.println(ss.peek());
        System.out.println(ss.remove());
        System.out.println(ss.remove());
        
        ss.add(1);
        ss.add(2);
        ss.add(3);
        ss.add(4);
        ss.add(5);
        
        System.out.println(ss.size());
        System.out.println(ss.isEmpty());
        
        ss.remove();
        ss.remove();
        ss.remove();
        ss.remove();
        ss.remove();
        
        System.out.println(ss.size());
        System.out.println(ss.isEmpty());
    }
    
    Node head = null;
    
    public void add(T t) {
        head = new Node(t, head);
    }
    
    public T remove() {
        if (head == null)
            return null;
        
        T obj = head.obj;
        head = head.next;
        return obj;
    }
    
    public T peek() {
        if (head == null)
            return null;
        
        return head.obj;
    }
    
    public int size() {
        Node current = head;
        int s = 0;
        
        while (current != null) {
            s++;
            current = current.next;
        }
        
        return s;
    }
    
    public boolean isEmpty() {
        return head == null;
    }
    
    private class Node {
        T obj;
        Node next;

        public Node(T obj, Node next) {
            this.obj = obj;
            this.next = next;
        }
    }
}
