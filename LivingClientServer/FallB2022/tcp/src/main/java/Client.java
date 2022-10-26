import java.net.Socket;

class Client {

    public static void main(String[] args) {
        String host = args[0];

        int port = Integer.parseInt(args[1]);

        try (Socket sock = new Socket(host, port)) {
            System.out.println("Host: " + host + "\nPort: " + sock.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
