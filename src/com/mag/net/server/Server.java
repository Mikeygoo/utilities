//<editor-fold defaultstate="collapsed" desc="owner">
/**
 * This file was created by and is maintained by ::Kevin Siouve:: Who has
 * immediate ownership over all classes in packages com.mag.* and org.jelphi
 * (with noted exceptions.)
 */
//</editor-fold>
package com.mag.net.server;

import com.mag.data.input.GenericDataInput;
import com.mag.data.input.StreamWrapperDataInputStream;
import com.mag.data.output.ByteArrayDataOutputStream;
import com.mag.data.output.DataSink;
import com.mag.data.output.GenericDataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @param <C> The client-capsule type.
 * @author Kevin August <KevinAnonymousXD@gmail.com>
 * @version 1.0
 */
public abstract class Server<C extends ClientCapsule> implements Runnable, Broadcaster<C> {
    private int defaultPort = 13370;
    private Queue<GenericDataOutput> streams = new LinkedBlockingQueue<>();
    private ServerSocket sock;

    /**
     * The default constructor, listening on port 1337.
     */
    public Server() {
        this(1337);
    }

    /**
     * Given a port, the server prepares to listen on it.
     * @param port the port to listen.
     */
    public Server(int port) {
        defaultPort = port;
    }

    private class ServerClientListener implements ClientListener<C> {
        private volatile C c = null;

        @Override
        public void putClient(C client) {
            c = client;
        }

        @Override
        public void run() { //TODO: Clean up?
            InputStream cstream;

            try {
                cstream = c.getSocket().getInputStream();
            } catch (IOException io) {
                System.out.println("Connection failed to be made with " + c.getSocket());
                return;
            }

            GenericDataInput gdis = new StreamWrapperDataInputStream(cstream);

            while (true) {
                //c.getSocket().getInputStream(); //check if the socket is STILL ALIVE!!
                byte sig = (byte) gdis.readByte();

                if (sig == -1) {
                    onClientDisconnect(c);
                    return;
                } else if (sig == 0) //sig = 0 is a keepalive.
                    continue;

                boolean caught = onPacketRecieved(sig, c, gdis);

                //System.out.println("Finished handling packet "+sig);
                if (!caught) {
                    onUnhandledByte(sig, c, gdis);
                }
            }
        }
    }

    /**
     * Queries an internal data provider in order to get a freed stream, or a newly created stream if none are available.
     * This can be much more efficient than instantiating a new stream every time, because it allows the recycle of old
     * data streams and the reuse every new generation.
     * @return
     */

    public synchronized GenericDataOutput claimStream() {
        GenericDataOutput dbos = streams.poll();

        if (dbos == null)
            return new ByteArrayDataOutputStream();

        return dbos;
    }

    /**
     * Releases and caches a stream to be easily claimed later. <b>Do not access the stream after it has been released. Treat
     * it as you might treat a deleted pointer in C++.</b>
     * @param dbos the stream to be released
     */

    public synchronized void releaseStream(GenericDataOutput dbos) {
        dbos.clear();
        streams.add(dbos);
    }

    private void beginListening(int port) throws IOException {
        sock = new ServerSocket(port);
        Socket incomming;
        ServerClientListener scl;
        C socketContainer;

        while (!sock.isClosed()) {
            try {
                incomming = sock.accept();
            } catch (SocketException socketE) {
                break;
            }

            scl = new ServerClientListener();
            socketContainer = prepareSocketContainer(incomming);
            scl.putClient(socketContainer);
            onClientConnect(incomming, socketContainer);
            new Thread(scl).start();
        }

        System.out.println("-- Server Stopped listening for Incomming Sockets --");
        sock.close();
    }

    @Override
    public final void run() {
        try {
            System.out.println("Began listening on port " + defaultPort);
            beginListening(defaultPort);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public final void disconnect(C client) {
        if (client.getSocket().isClosed())
            return;

        try {
            client.getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Prepare a socket container for the server's listeners to use. By default, you should construct the object on-the-fly, or possibly store it in a list, and return it so it may be used.
     *
     * @param socket the socket to be wrapped
     * @return a representation of the client inheriting ClientCapsule.
     */
    protected abstract C prepareSocketContainer(Socket socket);

    /**
     * Handle an unhandled packet. Usually, one should just print an error, but it is possible to stop the server if necessary.
     * @param address the signature
     * @param client the client
     * @param d the byte buffer
     */
    protected void onUnhandledByte(byte address, C client, GenericDataInput d) {
        System.out.println("Unhandled byte " + address + " from " + client.getSocket());
    }

    /**
     * Do any per-user init here, right before the listen-cycle is started.
     * @param socket the socket that is opened
     * @param client the client representation
     */
    protected abstract void onClientConnect(Socket socket, C client);

    /**
     * Handle a client death (disconnection) in this method.
     * @param client
     */
    protected void onClientDisconnect(C client) {

    }

    protected abstract boolean onPacketRecieved(byte signature, C client, GenericDataInput stream);

    /**
     * Write the buffer to the recipient
     * @param d data buffer
     * @param recipient recipient to write to
     */
    public void transmit(DataSink d, C recipient) {
        try {
            OutputStream o = recipient.getSocket().getOutputStream();

            synchronized (o) {
                d.writeTo(o);
            }
        } catch (IOException | RuntimeException ex) {
            try {
                recipient.getSocket().getInputStream().close();
                recipient.getSocket().getOutputStream().close();
                recipient.getSocket().close(); //this will cause it to disconnect thread-side.
            } catch (IOException ex1) {
                //I guess ignore.
            }
        }
    }

    @Override
    public void broadcast(DataSink g, C[] recipients) {
        for (C c : recipients)
            transmit(g, c);
    }

    public void stopListening() {
        try {
            sock.close();
        } catch (IOException ex) {
            //i don't really care...
        }
    }
}