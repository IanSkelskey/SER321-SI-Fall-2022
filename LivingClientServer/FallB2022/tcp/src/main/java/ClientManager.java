import org.json.JSONObject;
import utils.JsonUtils;
import utils.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static utils.Protocol.*;
import static utils.Protocol.createErrorResponse;

public class ClientManager implements Runnable {

    private static int clientCount = 0;

    private final int clientID;
    private final Socket socket;
    private final int port;

    private boolean isConnected;

    public ClientManager(ServerSocket serverSocket) throws IOException {
        this.socket = serverSocket.accept();
        clientCount += 1;
        this.clientID = clientCount;
        this.isConnected = true;
        System.out.println("New client has connected. The count is " + clientCount);
        this.port = serverSocket.getLocalPort();
    }

    @Override
    public void run() {
        try (
                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream()
        ) {

            System.out.println("New client has connected on port: " + this.port);

            while (this.isConnected) {
                byte[] requestBytes = NetworkUtils.Receive(in);
                JSONObject request = JsonUtils.fromByteArray(requestBytes);

                System.out.println("Received:\n" + request);

                JSONObject response = makeResponse(request);

                if (clientCount == 0) {
                    System.exit(0);
                }

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
                System.out.println("Client " + clientID + " has requested to disconnect.");
                clientCount -= 1;
                System.out.println("There are now " + clientCount + " clients connected.");
                this.isConnected = false;
            }
        }
        return createErrorResponse();
    }
}
