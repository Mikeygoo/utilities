//<editor-fold defaultstate="collapsed" desc="owner">
/**
 * This file was created by and is maintained by ::Kevin Siouve:: Who has
 * immediate ownership over all classes in packages com.mag.* and org.jelphi
 * (with noted exceptions.)
 */
//</editor-fold>

package com.mag.net.simple;

import com.mag.data.input.DataSource;
import com.mag.net.client.Client;
import com.mag.net.client.ServerCapsule;
import java.net.Socket;


/**
 * @author Kevin August <KevinAnonymousXD@gmail.com>
 * @version 1.0
 */
public abstract class SimpleClient extends Client<SimpleServerConnection> {
    public SimpleClient(String addr, int port) {
        super(addr, port);
    }

    public SimpleClient() {
    }

    @Override
    protected SimpleServerConnection prepareSocketContainer(Socket socket) {
        return new SimpleServerConnection(socket);
    }

    @Override
    protected void onSuccessfulConnection() {
        System.out.println("Successfully connected to the server!");
    }
}
