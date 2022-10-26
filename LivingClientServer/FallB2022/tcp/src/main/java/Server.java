import java.net.ServerSocket;
import java.net.Socket;

class Server {
    public static void main (String[] args) {

        int port = Integer.parseInt(args[0]);

        System.out.println("Server now listening on port: " + port);

        try (ServerSocket serverSocket = new ServerSocket(port); Socket socket = serverSocket.accept()) {
            System.out.println("New client has connected on port: " + port);
            // Handle Clients/ Keep Listening
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
