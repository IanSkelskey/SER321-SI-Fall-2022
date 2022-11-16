import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import buffers.Message.Request;
import buffers.Message.Response;

import static utils.Protocol.*;
import static utils.Protocol.createErrorResponse;

public class ClientManager implements Runnable {

    private boolean isConnected;
    private final int clientID;
    private final Socket socket;
    private final int port;


    public ClientManager(Socket socket) {
        this.socket = socket;
        this.clientID = Server.getClientCount();
        Server.addClient(clientID);
        this.isConnected = true;
        this.port = socket.getLocalPort();
    }

    @Override
    public void run() {
        try (
                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream()
        ) {
            System.out.println("New client has connected on port: " + this.port);
            System.out.println("The new count is " + Server.getClientCount());
            while (this.isConnected) {
                if (Server.getClientCount() == 0) {
                    System.exit(0);
                }

                Request request = Request.parseDelimitedFrom(in);

                System.out.println("Received:\n" + request.getType());

                Response response = makeResponse(request);

                if (!this.isConnected) break;

                response.writeDelimitedTo(out);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response makeResponse(Request request) {
        switch (request.getType()) {
            case ECHO -> {
                return createEchoResponse(request.getBody());
            }
            case REVERSE -> {
                return createReverseResponse(request.getBody());
            }
            case STRING_ARRAY -> {
                return createStringArrayResponse();
            }
            case JOIN -> {
                return createWelcomeResponse(request.getBody());
            }
            case EXIT -> {
                System.out.println("Client " + this.clientID + " has requested to disconnect.");
                Server.removeClient(this.clientID);
                System.out.println("There are now " + Server.getClientCount() + " clients connected.");
                this.isConnected = false;
            }
        }
        return createErrorResponse();
    }
}
