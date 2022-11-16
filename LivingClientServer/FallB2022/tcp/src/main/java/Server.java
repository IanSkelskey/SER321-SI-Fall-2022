import utils.BallotBox;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class Server {
    private static final List<Integer> clients = new ArrayList<>();
    private static final List<Integer> voters = new ArrayList<>();

    private static BallotBox ballotBox;

    public static boolean conductVote(int size) {
        ballotBox = new BallotBox(size);
        return ballotBox.getConsensus();
    }

    public static BallotBox getBallotBox() {
        return ballotBox;
    }

    public synchronized static void removeClient(int clientID) {
        clients.remove(clientID);
    }

    public static synchronized void removeVoter(int voterID) {
        voters.remove(voterID);
    }

    public synchronized static void addClient(int clientID) {
        clients.add(clientID);
    }

    public static synchronized void addVoter(int voterID) {
        voters.add(voterID);
    }

    public static synchronized int getClientCount() {
        return clients.size();
    }

    public static synchronized int getVoterCount() {
        return voters.size();
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
                pool.execute(new NewConnectionManager(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
