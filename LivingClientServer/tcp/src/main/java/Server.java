import java.net.*;

/**
 * Created referencing:
 * <a href="https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/networking/sockets/examples/EchoServer.java">Oracle EchoServer.java</a>
 */
class Server {

    public static void main(String[] args) throws Exception {

        int port = Integer.parseInt(args[0]);

        System.out.println("Server now listening on port: " + port);

        ServerSocket serverSocket = new ServerSocket(port);

        boolean spinning = true;

        while(spinning) {
            try {
                ClientHandler clientHandler = new ClientHandler(serverSocket);
                new Thread(clientHandler).start();
                ClientHandler.printCount();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}



