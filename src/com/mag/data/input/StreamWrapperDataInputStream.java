//<editor-fold defaultstate="collapsed" desc="owner">
/**
 * This file was created by and is maintained by ::Kevin Siouve:: Who has
 * immediate ownership over all classes in packages com.mag.* and org.jelphi
 * (with noted exceptions.)
 */
//</editor-fold>

package com.mag.data.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Kevin August <KevinAnonymousXD@gmail.com>
 * @version 1.0
 */
public class StreamWrapperDataInputStream extends GenericDataInput {
    private InputStream is;

    public StreamWrapperDataInputStream(InputStream i) {
        is = i;
    }

    @Override
    public int read() {
        try {
            return is.read();
        } catch (Exception ex) {

        }

        return -1;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
