package com.mag.datastructures;

import java.io.File;
import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Scanner;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;

public class HasherTable<K, V> implements java.util.Map<K, V> {
    public static final int SMALLEST_SIZE = 8;

    @SuppressWarnings("unchecked")
    EntryNode[] hashes = (EntryNode[]) Array.newInstance(EntryNode.class, SMALLEST_SIZE);
    boolean resizeDisabled = false;
    int totalBuckets = SMALLEST_SIZE, occupiedBuckets = 0, elements = 0;
    float rehashFactor = 1.75f, reduceFactor = 0.5f, occupationLoad = 2.0f;
    int mods = 0; //modifications

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Object o = new Object(); // just a raw value. Don't worry 'bout it.
        HasherTable<String, Object> h = new HasherTable<>();

        while (true) {
            System.out.print("> ");
            String n = s.next();
            h.put(n, o);

            for (int i = 0; i < h.totalBuckets; i++) {
                if (h.hashes[i] == null)
                    continue;

                System.out.print(i + ": ");

                for (HasherTable<String, Object>.EntryNode node = h.hashes[i]; node != null; node = node.next) {
                    System.out.print(node.key);
                    System.out.print(node.next != null ? "->" : "\n");
                }
            }

            System.out.println();
        }
    }

    @Override
    public V get(Object key) {
        if (key == null)
            throw new IllegalArgumentException();

        int hash = key.hashCode() % totalBuckets;
        EntryNode list = hashes[hash];

        for (EntryNode e = list; e != null; e = e.next) {
            if (e.key.equals(key))
                return e.value;
        }

        return null;
    }

    @Override
    public V put(K key, V value) {
        if (key == null || value == null)
            throw new IllegalArgumentException();

        int hash = (key.hashCode()) % totalBuckets;
        hash = hash * (hash < 0 ? -1 : 1); //save me from negative hashes!!
        System.out.println(String.format("They key for %s is %d, modded %d is %d", key, key.hashCode(), totalBuckets, hash));
        EntryNode list = hashes[hash];

        for (EntryNode e = list; e != null; e = e.next) {
            if (e.key.equals(key)) {
                V oldVal = e.value;
                e.value = value;
                mods++; //modified
                return oldVal;
            }
        }

        EntryNode newEntry = new EntryNode(); //otherwise, if there isn't anything to copy...

        if (list == null)
            occupiedBuckets++;

        newEntry.key = key;
        newEntry.value = value;
        newEntry.next = list; //link in; if null it won't do anything.
        hashes[hash] = newEntry;
        elements++;
        mods++; //modified
        tryResize();
        return null;
    }

    @Override
    public V remove(Object key) {
        if (key == null)
            throw new IllegalArgumentException();

        int hash = key.hashCode() % totalBuckets;
        EntryNode list = hashes[hash];

        if (list == null)
            return null;

        if (list.key.equals(key)) {
            hashes[hash] = list.next;

            if (list.next == null) {
                occupiedBuckets--;
            }

            elements--;
            mods++;
            return list.value;
        } else {
            for (EntryNode e = list; e.next != null; e = e.next) {
                if (e.next.key.equals(key)) {
                    V old = e.next.value;
                    e.next = e.next.next;
                    elements--;

                    mods++;
                    tryResize(); //resize the hash if necessary.
                    return old;
                }
            }
        }

        return null;
    }

    @Override
    public int size() {
        return elements;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void clear() {
        elements = 0;

        for (int i = 0; i < hashes.length; i++) {
            for (EntryNode e = hashes[i]; e != null; e = e.next) {
                e.key = null;
                e.value = null;
            }

            hashes[i] = null;
        }

        mods++;
        tryResize();
    }

    private void tryResize() {
        if (resizeDisabled)
            return;

        float totalLoad = elements * 1.0f / totalBuckets/*, distributionLoad = elements * 1.0f / occupiedBuckets*/;
        System.out.println("Load factor is " + totalLoad);
        int newsize = totalBuckets;

        while (true) {
            if (totalLoad > rehashFactor) {
                newsize = (int)(newsize * 2);
                totalLoad = (int)(totalLoad / 2);
            } else if (totalLoad < reduceFactor) {
                if (newsize / 2 < SMALLEST_SIZE) {
                    newsize = SMALLEST_SIZE;
                    break;
                }

                newsize = (int)(newsize / 2);
                totalLoad = (int)(totalLoad * 2);
            } else
                break;
        }

        if (totalBuckets != newsize) { //don't resize if it's not changing the size or rehashing
            totalBuckets = newsize;
            occupiedBuckets = 0;
            System.out.println("Rehashes to size: " + newsize);
            EntryNode[] newHashes = (EntryNode[]) Array.newInstance(EntryNode.class, newsize);

            for (int i = 0; i < hashes.length; i++) {
                for (EntryNode e = hashes[i]; e != null; /*check method body for e = e.next*/) {
                    EntryNode node = e;
                    e = e.next;
                    int newHash = (node.getKey().hashCode()) % newsize;
                    newHash = newHash * (newHash < 0 ? -1 : 1);
                    node.next = newHashes[newHash];

                    if (newHashes[newHash] == null)
                        occupiedBuckets++;

                    newHashes[newHash] = node;
                }
            }

            hashes = newHashes;
            mods = -10;
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Iterator<Entry<K, V>> i = iterator(); i.hasNext();) {
            Entry<K, V> e = i.next();

            if (e.getValue().equals(value))
                return true;
        }

        return false;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        boolean saveResizeDisabled = resizeDisabled; //just in case I'm nesting it.
        resizeDisabled = true;

        for (Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }

        resizeDisabled = saveResizeDisabled;
    }

    @Override
    public Set<K> keySet() {
        return new KeySet();
    }

    @Override
    public Collection<V> values() {
        return new Values();
    }

    private Iterator<Entry<K, V>> iterator() {
        return new Iterator<Entry<K, V>>() {
            EntryNode n = null;
            int index = -1;
            int expectedMods = mods;

            @Override
            public boolean hasNext() {
                if (n != null && n.next != null)
                    return true;

                for (int i = index + 1; i < hashes.length; i++)
                    if (hashes[i] != null)
                        return true;

                return false;
            }

            @Override
            public Entry<K, V> next() {
                checkConcurrentModification();

                if (n != null && n.next != null)
                    return (n = n.next);

                index++;

                for (; index < hashes.length; index++) {
                    if (hashes[index] != null)
                        return (n = hashes[index]);
                }

                return null;
            }

            @Override
            public void remove() {
                checkConcurrentModification();

                if (n == null)
                    throw new IllegalStateException();

                HasherTable.this.remove(n.key);
                expectedMods = mods;
            }

            private void checkConcurrentModification() {
                if (mods != expectedMods)
                    throw new ConcurrentModificationException();
            }
        };
    }

    private class EntryNode implements Entry<K, V> {
        private K key;
        private V value;
        private EntryNode next = null;

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldvalue = this.value;
            this.value = value;
            return oldvalue;
        }
    }

    private class EntrySet extends AbstractSet<Entry<K, V>> {
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return iterator();
        }

        @Override
        public int size() {
            return HasherTable.this.size();
        }

        @Override
        @SuppressWarnings("Unchecked")
        public boolean remove(Object o) {
            if (!(o instanceof Entry))
                throw new IllegalArgumentException();

            return HasherTable.this.remove(((Entry<K, V>) o).getKey()) != null;
        }

        @Override
        public void clear() {
            HasherTable.this.clear();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Entry))
                throw new IllegalArgumentException();

            Entry<K, V> e = (Entry<K, V>) o;
            return HasherTable.this.get(e.getKey()).equals(e.getValue());
        }
    }

    private class KeySet extends AbstractSet<K> {
        @Override
        public Iterator<K> iterator() {
            return new Iterator<K>() {
                Iterator<Entry<K, V>> iterator = HasherTable.this.iterator();

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public K next() {
                    return iterator.next().getKey();
                }

                @Override
                public void remove() {
                    iterator.remove();
                }
            }; //simple wrapping of HasherTable.iterator()
        }

        @Override
        public int size() {
            return HasherTable.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return HasherTable.this.containsKey(o);
        }

        @Override
        @SuppressWarnings("suspicious")
        public boolean remove(Object o) {
            return HasherTable.this.remove(o) != null;
        }

        @Override
        public void clear() {
            HasherTable.this.clear();
        }
    }

    private class Values extends AbstractCollection<V> {
        @Override
        public Iterator<V> iterator() {
            return new Iterator<V>() {
                Iterator<Entry<K, V>> iterator = HasherTable.this.iterator();

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public V next() {
                    return iterator.next().getValue();
                }

                @Override
                public void remove() {
                    iterator.remove();
                }
            }; //simple wrapping of HasherTable.iterator()
        }

        @Override
        public int size() {
            return HasherTable.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return HasherTable.this.containsValue(o);
        }
    }

    @Override
    public int hashCode() {
        int ret = 0;

        for (Iterator<Entry<K, V>> i = iterator(); i.hasNext();) {
            Entry<K, V> e = i.next();
            ret += e.hashCode();
        }

        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        HasherTable<K, V> h = (HasherTable<K, V>) obj;
        Set<Entry<K, V>> otherSet = h.entrySet();

        for (Entry<K, V> e : this.entrySet())
            if (!otherSet.contains(e))
                return false;

        return true;
    }
}
