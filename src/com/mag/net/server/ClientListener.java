//<editor-fold defaultstate="collapsed" desc="owner">
/**
 * This file was created by and is maintained by ::Kevin Siouve:: Who has
 * immediate ownership over all classes in packages com.mag.* and org.jelphi
 * (with noted exceptions.)
 */
//</editor-fold>
package com.mag.net.server;

/**
 * @author Kevin August <KevinAnonymousXD@gmail.com>
 * @version 1.0
 */
interface ClientListener<C> extends Runnable {
    void putClient(C client);
}
