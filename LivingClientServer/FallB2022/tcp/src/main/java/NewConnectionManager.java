import buffers.Message;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static utils.Protocol.*;

public class NewConnectionManager implements Runnable {
    private final Socket socket;

    public NewConnectionManager(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            OutputStream out = this.socket.getOutputStream();
            InputStream in = this.socket.getInputStream();

            Message.Request request = Message.Request.parseDelimitedFrom(in);
            Message.Response response = makeResponse(request);
            response.writeDelimitedTo(out);
            if (request.getBody().equalsIgnoreCase("client")) {
                new Thread(new ClientManager(this.socket)).start();
            } else if (request.getBody().equalsIgnoreCase("voter")) {
                new Thread(new VoterManager(this.socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message.Response makeResponse(Message.Request request) {
        if (request.getType() == Message.Type.JOIN) {
            return createWelcomeResponse(request.getBody());
        }
        return createErrorResponse();
    }

}
