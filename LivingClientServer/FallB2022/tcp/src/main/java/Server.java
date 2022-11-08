import java.net.ServerSocket;

class Server {


    public static void main(String[] args) {

        int port = Integer.parseInt(args[0]);

        System.out.println("Server started. Waiting for client to connect...");

        try (
                ServerSocket serverSocket = new ServerSocket(port);
        ) {
            while (true) {
                ClientManager clientManager = new ClientManager(serverSocket);
                new Thread(clientManager).start();
            }
        } catch (Exception e) {

        }

    }


}
