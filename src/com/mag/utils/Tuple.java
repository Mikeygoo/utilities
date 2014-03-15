//<editor-fold defaultstate="collapsed" desc="owner">
/**
 * This file was created by and is maintained by ::Kevin Siouve:: Who has
 * immediate ownership over all classes in packages com.mag.* and org.jelphi
 * (with noted exceptions.)
 */
//</editor-fold>

package com.mag.utils;

import java.util.Iterator;


/**
 * @author Kevin August <KevinAnonymousXD@gmail.com>
 * @version 1.0
 */
public class Tuple<T> implements Iterable<T> {
    Object[] obj;

    public Tuple(T... t) {
        obj = t;
    }

    public Tuple(Class<T> clazz, T... t) {
        obj = t;

        for (Object object : obj)
            if (!clazz.isAssignableFrom(object.getClass()))
                throw new IllegalArgumentException(object.getClass() + " <not an instance of> " + clazz);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < obj.length;
            }

            @Override
            public T next() {
                return (T)obj[i++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Removal not supported!");
            }
        };
    }
}
