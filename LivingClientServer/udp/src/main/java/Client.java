import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

class Client {

    public static void main(String[] args) throws Exception {
        String host = args[0];

        int port = Integer.parseInt(args[1]);
        InetAddress ip = InetAddress.getByName(host);

        try (
                DatagramSocket sock = new DatagramSocket();
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Host: " + host + "\nPort: " + port);
            System.out.println("Enter a message to send to the server!\n");
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                byte[] buf = userInput.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, port);
                sock.send(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
