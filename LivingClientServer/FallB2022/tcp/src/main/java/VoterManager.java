import buffers.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

import static utils.Protocol.createVoteResponse;

public class VoterManager implements Runnable {

    private final Socket socket;
    private final int voterID;
    private final int port;
    private final boolean isConnected;


    public VoterManager(Socket socket) {
        this.socket = socket;
        this.voterID = Server.getVoterCount();
        Server.addVoter(this.socket);
        this.isConnected = true;
        this.port = socket.getLocalPort();
    }

    @Override
    public void run() {
        try (
                OutputStream out = this.socket.getOutputStream();
                InputStream in = this.socket.getInputStream()
        ) {
            System.out.println("New voter has connected on port: " + this.port);
            System.out.println("The new voter count is " + Server.getClientCount());
            while (this.isConnected) {
                Message.Request request = Message.Request.parseDelimitedFrom(in);
                Message.Response response = makeResponse(request);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Message.Response makeResponse(Message.Request request) {
        return createVoteResponse(true);
    }
}
