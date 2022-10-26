import java.net.*;

class Server {
    public static void main(String[] args) throws Exception {

        int port = Integer.parseInt(args[0]);

        System.out.println("Server now listening on port: " + port);

        byte[] buffer = new byte[65535];

        try (
                DatagramSocket sock = new DatagramSocket(port)
        ) {
            while (true) {
                DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
                sock.receive(inPacket);
                System.out.println("Received: " + bytesToString(buffer));
                buffer = new byte[65535];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Could be useful in NetworkUtils?
    private static String bytesToString(byte[] bytes) {
        if (bytes == null)
            return null;
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (bytes[i] != 0) {
            builder.append((char) bytes[i]);
            i++;
        }
        return builder.toString();
    }
}
