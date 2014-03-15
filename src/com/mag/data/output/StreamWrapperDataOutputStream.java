//<editor-fold defaultstate="collapsed" desc="owner">
/**
 * This file was created by and is maintained by ::Kevin Siouve:: Who has
 * immediate ownership over all classes in packages com.mag.* and org.jelphi
 * (with noted exceptions.)
 */
//</editor-fold>

package com.mag.data.output;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Kevin August <KevinAnonymousXD@gmail.com>
 * @version 1.0
 */
public class StreamWrapperDataOutputStream extends GenericDataOutput {
    private OutputStream o;

    public StreamWrapperDataOutputStream(OutputStream o) {
        writeTo(o);
    }

    public StreamWrapperDataOutputStream(File f) {
        if (f.canWrite())
            try {
                writeTo(new FileOutputStream(f));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(StreamWrapperDataOutputStream.class.getName()).log(Level.SEVERE, null, ex);
            }
        else
            throw new RuntimeException("File: \"" + f + "\" is unwritable!");
    }

    @Override
    public void writeByte(int i) {
        try {
            o.write(i);
        } catch (IOException ex) {
            Logger.getLogger(StreamWrapperDataOutputStream.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void writeTo(OutputStream o) {
        this.o = o;
    }

    @Override
    public void clear() {

    }
}
