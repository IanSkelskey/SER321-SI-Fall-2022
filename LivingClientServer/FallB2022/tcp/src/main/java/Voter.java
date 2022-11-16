import buffers.Message;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static utils.Protocol.createJoinRequest;

public class Voter {

    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try (
                Socket sock = new Socket(host, port);
                OutputStream out = sock.getOutputStream();
                InputStream in = sock.getInputStream()
        ) {
            System.out.println("Host: " + host + "\nPort: " + port);

            Message.Request joinRequest = createJoinRequest("Voter");
            joinRequest.writeDelimitedTo(out);

            Message.Response response = Message.Response.parseDelimitedFrom(in);
            System.out.println("Response: " + response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
