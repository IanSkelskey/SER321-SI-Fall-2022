import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class Client {

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[1]);
        InetAddress host = InetAddress.getByName(args[0]);

        try (
                DatagramSocket sock = new DatagramSocket();
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Host: " + host + "\nPort: " + port);
            System.out.println("Enter a message to send to the server!\n");
            String userInput = stdIn.readLine();
            System.out.println("You entered: " + userInput);
            byte[] buffer = userInput.getBytes();
            DatagramPacket outPacket = new DatagramPacket(buffer, buffer.length, host, port);
            sock.send(outPacket);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
