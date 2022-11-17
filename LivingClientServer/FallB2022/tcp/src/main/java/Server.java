import buffers.Message;
import utils.BallotBox;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static utils.Protocol.createVoteRequest;

class Server {
    private static final List<Integer> clients = new ArrayList<>();
    private static final List<Socket> voters = new ArrayList<>();

    public static void sendToAllVoters(Message.Request request) {
        int headCount = getVoterCount();
        ballotBox = new BallotBox(headCount);
        for(Socket s: voters) {
            try {
                OutputStream out = s.getOutputStream();
                request.writeDelimitedTo(out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static BallotBox ballotBox;

    public static void castVote(boolean vote) {
        ballotBox.receiveVote(vote);
    }

    // TODO: Complete
    public synchronized static boolean conductVote() {
        sendToAllVoters(createVoteRequest());
        return ballotBox.getConsensus();
    }

    public synchronized static BallotBox getBallotBox() {
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

    public static synchronized void addVoter(Socket voterSocket) {
        voters.add(voterSocket);
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
