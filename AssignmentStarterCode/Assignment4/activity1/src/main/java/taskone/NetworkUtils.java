/**
  File: NetworkUtils.java
  Author: Student in Fall 2020B
  Description: NetworkUtils class in package taskone.
*/

package taskone;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Class: NetworkUtils 
 * Description: NetworkUtils for send/read/receive.
 */
public class NetworkUtils {
    // https://mkyong.com/java/java-convert-byte-to-int-and-vice-versa/
    public static byte[] intToBytes(final int data) {
        return new byte[] { (byte) ((data >> 24) & 0xff), (byte) ((data >> 16) & 0xff), 
            (byte) ((data >> 8) & 0xff), (byte) ((data >> 0) & 0xff), };
    }

    // https://mkyong.com/java/java-convert-byte-to-int-and-vice-versa/
    public static int bytesToInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8)
                | ((bytes[3] & 0xFF) << 0);
    }
    
    /**
     * send the bytes on the stream.
     */
    public static void send(OutputStream out, byte... bytes) throws IOException {
        out.write(intToBytes(bytes.length));
        out.write(bytes);
        out.flush();
    }

    // read the bytes on the stream
    private static byte[] read(InputStream in, int length) throws IOException {
        byte[] bytes = new byte[length];
        int bytesRead = 0;
        try {
            bytesRead = in.read(bytes, 0, length);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if (bytesRead != length) {
            return null;
        }

        return bytes;
    }

    // first 4 bytes we read give us the length of the message we are about to
    // receive
    // next we call read again with the length of the actual bytes in the data we
    // are interested in
    /** 
     * Receive the bytes on the stream.
     */
    public static byte[] receive(InputStream in) throws IOException {
        byte[] lengthBytes = read(in, 4);
        if (lengthBytes == null) {
            return new byte[0];
        }
        int length = NetworkUtils.bytesToInt(lengthBytes);
        byte[] message = read(in, length);
        if (message == null) {
            return new byte[0];
        }
        return message;
    }
}
