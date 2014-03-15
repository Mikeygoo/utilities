//<editor-fold defaultstate="collapsed" desc="owner">
/**
 * This file was created by and is maintained by ::Kevin Siouve:: Who has
 * immediate ownership over all classes in packages com.mag.* and org.jelphi
 * (with noted exceptions.)
 */
//</editor-fold>

package com.mag.data.input;

import com.mag.data.output.DataSink;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Kevin August <KevinAnonymousXD@gmail.com>
 * @version 1.0
 */
public class LoopbackDataSource extends GenericDataInput implements DataSink {
    private final static Charset ascii = Charset.forName("US-ASCII");
    private final static Charset unicode = Charset.forName("UTF8");

    LinkedBlockingQueue<Integer> stream = new LinkedBlockingQueue<Integer>();

    @Override
    public void writeByte(int i) {
        try {
            stream.put(i);
        } catch (InterruptedException ex) {
            Logger.getLogger(LoopbackDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void writeShort(short s) { // 2 bytes
        writeByte((s >>> 8) & 0xff);
        writeByte(s & 0xff);
    }

    @Override
    public void writeInt(int i) { // 4 bytes
        writeByte((i >>> 24) & 0xff);
        writeByte((i >>> 16) & 0xff);
        writeByte((i >>> 8) & 0xff);
        writeByte(i & 0xff);
    }

    @Override
    public void writeLong(long l) { // 8 bytes
        writeByte((int)((l >>> 56) & 0xff));
        writeByte((int)((l >>> 48) & 0xff));
        writeByte((int)((l >>> 40) & 0xff));
        writeByte((int)((l >>> 32) & 0xff));
        writeByte((int)((l >>> 24) & 0xff));
        writeByte((int)((l >>> 16) & 0xff));
        writeByte((int)((l >>>  8) & 0xff));
        writeByte((int)(l & 0xff));
    }

    @Override
    public void writeFloat(float f) { // 4 bytes
        //System.out.println("writing float "+f);
        writeInt(Float.floatToIntBits(f));
    }

    @Override
    public void writeDouble(double d) { // 8 bytes
        writeLong(Double.doubleToLongBits(d));
    }

    @Override
    public void writeBoolean(boolean b) { // 1 byte
        writeByte(b ? 0x1 : 0x0);
    }

    @Override
    public void writeChar(char c) { //2 bytes
        writeByte((c >>> 8) & 0xff);
        writeByte(c & 0xff);

    }

    @Override
    public void writeASCIIString(String asciistr) {
        for (char c : asciistr.toCharArray()) {
            System.out.println("Writing: " + (c & 0xff));
            writeByte((byte)(c & 0xff)); //LEAST SIGNIFICANT BYTE.
        }
    }

    @Override
    public void writeLengthASCIIString(String asciistr) {
        writeShort((short)asciistr.length());
        writeASCIIString(asciistr);
    }

    @Override
    public void writeNullTerminatedASCIIString(String asciistr) {
        writeASCIIString(asciistr);
        writeByte(-1);
    }

    @Override
    public void writeUnicodeString(String asciistr) {
        for (char c : asciistr.toCharArray()) {
            writeChar(c);
        }
    }

    @Override
    public void writeLengthUnicodeString(String asciistr) {
        writeShort((short)asciistr.length());
        writeUnicodeString(asciistr);
    }

    @Override
    public void writeNullTerminatedUnicodeString(String asciistr) {
        writeUnicodeString(asciistr);
        writeChar('\0');
    }

    @Override
    public void writeTo(OutputStream o) {
        //do nothing!
    }

    @Override
    public void clear() {
        stream.clear();
    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public int read() {
        try {
            return stream.take();
        } catch (InterruptedException ex) {
            Logger.getLogger(LoopbackDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

    public OutputStream toOutputStream() {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                writeByte(b);
            }
        };
    }
}
