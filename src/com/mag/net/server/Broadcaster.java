//<editor-fold defaultstate="collapsed" desc="owner">
/**
 * This file was created by and is maintained by ::Kevin Siouve:: Who has
 * immediate ownership over all classes in packages com.mag.* and org.jelphi
 * (with noted exceptions.)
 */
//</editor-fold>
package com.mag.net.server;

import com.mag.data.output.DataSink;

/**
 * @param <C>
 * @author Kevin August <KevinAnonymousXD@gmail.com>
 * @version 1.0
 */
public interface Broadcaster<C> {
    /**
     * Broadcasts the
     * @param g the buffer
     */
    void broadcastAll(DataSink g);
    /**
     * Writes the buffer to each of the recipients.
     * @param g the buffer
     * @param recipients array of recipients
     */
    void broadcast(DataSink g, C[] recipients);

}
