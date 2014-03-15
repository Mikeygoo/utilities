//<editor-fold defaultstate="collapsed" desc="owner">
/**
 * This file was created by and is maintained by ::Kevin Siouve:: Who has
 * immediate ownership over all classes in packages com.mag.* and org.jelphi
 * (with noted exceptions.)
 */
//</editor-fold>

package com.mag.net.simple;

import com.mag.data.input.DataSource;
import com.mag.data.output.DataSink;
import com.mag.net.server.Server;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Kevin August <KevinAnonymousXD@gmail.com>
 * @version 1.0
 */
public abstract class SimpleServer extends Server<SimpleClientConnection> {
    protected List<SimpleClientConnection> db = new LinkedList<>();

    public SimpleServer() {
    }

    public SimpleServer(int port) {
        super(port);
    }

    @Override
    protected final SimpleClientConnection prepareSocketContainer(Socket socket) {
        SimpleClientConnection scc = new SimpleClientConnection(socket);
        db.add(scc);
        return scc;
    }

    @Override
    public final void broadcastAll(DataSink g) {
        for (SimpleClientConnection sc : db) {
            try {
                OutputStream o = sc.getSocket().getOutputStream();

                synchronized (o) {
                    g.writeTo(sc.getSocket().getOutputStream());
                }
            } catch (IOException ex) {
                Logger.getLogger(SimpleServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public final void broadcast(DataSink g, SimpleClientConnection[] recipients) {
        for (SimpleClientConnection sc : recipients) {
            try {
                OutputStream o = sc.getSocket().getOutputStream();

                synchronized (o) {
                    g.writeTo(sc.getSocket().getOutputStream());
                }
            } catch (IOException ex) {
                Logger.getLogger(SimpleServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }



    @Override
    protected final void onClientDisconnect(SimpleClientConnection client) {
        db.remove(client);
        onDisconnection(client);
    }

    protected void onDisconnection(SimpleClientConnection client) {

    }
}
