package com.mag.net.client;

import com.mag.data.input.GenericDataInput;
import com.mag.data.input.StreamWrapperDataInputStream;
import com.mag.data.output.ByteArrayDataOutputStream;
import com.mag.data.output.DataSink;
import com.mag.data.output.GenericDataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public abstract class Client<S extends ServerCapsule> implements Runnable {
    private String address = "localhost";
    private int port = 1337;
    private S server;

    private BlockingQueue<GenericDataOutput> streams = new ArrayBlockingQueue<>(100);

    /**
     * The default constructor, attaching to localhost:1337.
     */
    public Client() {
        this("localhost", 1337);
    }

    /**
     * @param addr the address of the server
     * @param port the port of the server.
     */
    public Client(String addr, int port) {
        address = addr;
        this.port = port;
    }

    /**
     * Begins listening on default port and address.
     */
    @Override
    public final void run() {
        beginListening(address, port);
    }

    public synchronized GenericDataOutput claimStream() {
        try {
            GenericDataOutput gdo = streams.poll(10, TimeUnit.MILLISECONDS);

            if (gdo == null)
                return new ByteArrayDataOutputStream();

            return gdo;
        } catch (InterruptedException ex) {
            return new ByteArrayDataOutputStream();
        }
    }

    public synchronized void releaseStream(GenericDataOutput dbos) {
        dbos.clear();

        try {
            streams.offer(dbos, 10, TimeUnit.MILLISECONDS); //it will just dispose of it I guess...
        } catch (InterruptedException ex) {

        }
    }

    /**
     * Gets the server-pointing socket capsule.
     * @return
     */
    protected final S getServer() {
        return server;
    }

    private void beginListening(String address, int port) {
        Socket s;

        try {
            s = new Socket(address, port);
        } catch (UnknownHostException ex) {
            onUnsuccessfulConnection();
            return;
        } catch (IOException ex) {
            onUnsuccessfulConnection();
            return;
        }

        server = prepareSocketContainer(s);

        onSuccessfulConnection();

        InputStream cstream;

        try {
            cstream = server.getSocket().getInputStream();
        } catch (IOException io) {
            System.out.println("Connection failed to be made with " + server.getSocket());
            return;
        }

        GenericDataInput gdis = new StreamWrapperDataInputStream(cstream);

        while (true) {
            //c.getSocket().getInputStream(); //check if the socket is STILL ALIVE!!
            byte sig = (byte) gdis.readByte();

            if (sig == -1) {
                onDisconnect();
                return;
            }

            boolean caught = onPacketRecieved(sig, gdis);

            //System.out.println("Finished handling packet "+sig);
            if (!caught) {
                onUnhandledByte(sig, gdis);
            }
        }
    }

    private void foundPacket(byte address, GenericDataInput g) {
        boolean ret = onPacketRecieved(address, g);

        if (!ret)
            onUnhandledByte(address, g);
    }

    /**
     * Transmits the buffer to the server.
     * @param d the byte buffer to be transmitted.
     */
    public final void transmit(DataSink d) {
        try {
            OutputStream os = server.getSocket().getOutputStream();

            synchronized (os) {
                d.writeTo(os);
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Prepare a socket-encapsulation of the socket object.
     * @param socket the socket to wrap
     * @return the encapsulated ServerCapsule
     */
    protected abstract S prepareSocketContainer(Socket socket);

    /**
     * Handles an unhandled packet
     * @param address the packet signature
     * @param d the byte buffer
     */
    protected void onUnhandledByte(byte address, GenericDataInput d) {
        System.out.println("Unhandled byte recieved: " + address);
    }

    /**
     * Initializing the client when, and if, socket connection is successful.
     */
    protected abstract void onSuccessfulConnection();

    /**
     * Called when, or if, a unsuccessful connection is made.
     */
    protected void onUnsuccessfulConnection() {

    }

    /**
     * Called on disconnection, either graceful or violent, from the server.
     */
    protected abstract void onDisconnect();

    /**
     * Called when a packet is recieved.
     * @param signature the signature byte
     * @param stream the byte buffer
     * @return true, if the packet is consumed. false, if it is unknown.
     */
    protected abstract boolean onPacketRecieved(byte signature, GenericDataInput stream);
}
