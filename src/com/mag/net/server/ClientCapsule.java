//<editor-fold defaultstate="collapsed" desc="owner">
/**
 * This file was created by and is maintained by ::Kevin Siouve:: Who has
 * immediate ownership over all classes in packages com.mag.* and org.jelphi
 * (with noted exceptions.)
 */
//</editor-fold>
package com.mag.net.server;

import java.net.Socket;

/**
 * @author Kevin August <KevinAnonymousXD@gmail.com>
 * @version 1.0
 */
public interface ClientCapsule {
    /**
     * Gets the socket of the client, so the server may send and recieve from the client.
     * @return the socket
     */
    Socket getSocket();
}
