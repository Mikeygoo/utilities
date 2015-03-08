package com.mag.arrayshow;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class TimeSortingMethod extends SortingMethod {
    private AtomicInteger index = new AtomicInteger(0);
    private volatile boolean sorting = true;

    public TimeSortingMethod() {
        super("Time Sort");
    }
    
    @Override
    protected void sort(final int[] a) {
        index.set(0);
        ArrayList<Callable<Void>> list = new ArrayList<Callable<Void>>();
        for (final int i : a) {
            Callable<Void> thread = new Callable<Void>() {
                @Override
                public Void call() {
                    try {
                        Thread.sleep(i*50);
                        synchronized (this) {
                            int where = index.getAndIncrement();
                            set(i, where);
                            
                            if (where + 1 == a.length)
                                sorting = false;
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TimeSortingMethod.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return null;
                }
            };
            list.add(thread);
        }
        
        ExecutorService ex = Executors.newFixedThreadPool(a.length);
        
        try {
            ex.invokeAll(list);
        } catch (InterruptedException ex1) {
            Logger.getLogger(TimeSortingMethod.class.getName()).log(Level.SEVERE, null, ex1);
        }

        while (sorting);
        
        ex.shutdown();
    }
}
