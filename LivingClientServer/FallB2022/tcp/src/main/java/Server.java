import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class Server {
    private static final List<Integer> clients = new ArrayList<>();

    public synchronized static void removeClient(int clientID) {
        clients.remove(clientID);
    }

    public synchronized static void addClient(int clientID) {
        clients.add(clientID);
    }

    public static synchronized int getClientCount() {
        return clients.size();
    }

    public static void main(String[] args) {

        int port = Integer.parseInt(args[0]);
        int limit = Integer.parseInt(args[1]);

        System.out.println("Server started. Waiting for client to connect...");

        try (
                ServerSocket serverSocket = new ServerSocket(port);
        ) {
            Executor pool = Executors.newFixedThreadPool(limit);
            while (true) {
                Socket socket = serverSocket.accept();
                pool.execute(new ClientManager(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
