import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static util.NetworkUtils.bytesToString;

class Server {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);

        System.out.println("Server now listening on port: " + port);

        byte[] buffer = new byte[65535];

        try (
                DatagramSocket sock = new DatagramSocket(port)
        ) {
            DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
            sock.receive(inPacket);
            System.out.println("Received: " + bytesToString(buffer));
            buffer = new byte[65535];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
