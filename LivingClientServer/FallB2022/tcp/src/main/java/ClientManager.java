import org.json.JSONObject;
import utils.JsonUtils;
import utils.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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

                byte[] requestBytes = NetworkUtils.Receive(in);
                JSONObject request = JsonUtils.fromByteArray(requestBytes);

                System.out.println("Received:\n" + request);

                JSONObject response = makeResponse(request);

                if (!this.isConnected) break;

                byte[] output = JsonUtils.toByteArray(response);
                NetworkUtils.Send(out, output);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject makeResponse(JSONObject request) {
        switch (request.getString("type")) {
            case "echo" -> {
                return createEchoResponse(request.getString("body"));
            }
            case "reverse" -> {
                return createReverseResponse(request.getString("body"));
            }
            case "string array" -> {
                return createStringArrayResponse();
            }
            case "exit" -> {
                System.out.println("Client " + this.clientID + " has requested to disconnect.");
                Server.removeClient(this.clientID);
                System.out.println("There are now " + Server.getClientCount() + " clients connected.");
                this.isConnected = false;
            }
        }
        return createErrorResponse();
    }
}
