//<editor-fold defaultstate="collapsed" desc="owner">
/**
 * This file was created by and is maintained by ::Kevin Siouve:: Who has
 * immediate ownership over all classes in packages com.mag.* and org.jelphi
 * (with noted exceptions.)
 */
//</editor-fold>

package com.mag.utils;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Kevin August <KevinAnonymousXD@gmail.com>
 * @version 1.0
 */
public class TemporaryFile implements Closeable {
    private File f = null;

    public TemporaryFile() {
        this("m" + System.nanoTime() + "derp");
    }

    public TemporaryFile(String name) {
        try {
            f = File.createTempFile(name, "tmp");
        } catch (IOException ex) {
            Logger.getLogger(TemporaryFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public InputStream getInputStream() {
        try {
            return new FileInputStream(f);
        } catch (FileNotFoundException | NullPointerException ex) {
            Logger.getLogger(TemporaryFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public OutputStream getOutputStream() {
        try {
            return new FileOutputStream(f);
        } catch (FileNotFoundException | NullPointerException e) {
            Logger.getLogger(TemporaryFile.class.getName()).log(Level.SEVERE, null, e);
        }

        return null;
    }

    @Override
    public void close() throws IOException {
        f = null;
    }
}
