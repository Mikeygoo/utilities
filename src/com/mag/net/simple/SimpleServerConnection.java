//<editor-fold defaultstate="collapsed" desc="owner">
/**
 * This file was created by and is maintained by ::Kevin Siouve:: Who has
 * immediate ownership over all classes in packages com.mag.* and org.jelphi
 * (with noted exceptions.)
 */
//</editor-fold>

package com.mag.net.simple;

import com.mag.net.client.ServerCapsule;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Kevin August <KevinAnonymousXD@gmail.com>
 * @version 1.0
 */
public class SimpleServerConnection implements ServerCapsule {
    private Socket s;

    public SimpleServerConnection(Socket s) {
        this.s = s;
    }

    @Override
    public Socket getSocket() {
        return s;
    }

    public void close() {
        if (!s.isClosed()) {
            try {
                s.close();
            } catch (IOException ex) {
                Logger.getLogger(SimpleClientConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String toString() {
        return s.toString();
    }
}
