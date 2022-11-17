import buffers.Message;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

import static utils.Protocol.createJoinRequest;
import static utils.Protocol.createVoteResponse;

public class Voter {

    public static void main(String[] args) {
        final boolean vote;
        Random rand = new Random();
        vote = rand.nextBoolean();
        System.out.println("My vote is: " + vote);

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try (
                Socket sock = new Socket(host, port);
                OutputStream out = sock.getOutputStream();
                InputStream in = sock.getInputStream()
        ) {
            Server.addVoter(sock);
            System.out.println("Host: " + host + "\nPort: " + port);

            Message.Request joinRequest = createJoinRequest("Voter");
            joinRequest.writeDelimitedTo(out);

            Message.Response response = Message.Response.parseDelimitedFrom(in);
            System.out.println("Response: " + response.getBody());

            while (true) {
                Message.Request voteRequest = Message.Request.parseDelimitedFrom(in);
                System.out.println(voteRequest);
                Server.castVote(vote);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
