import org.json.JSONObject;
import utils.JsonUtils;
import utils.NetworkUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);

        System.out.println("Server started. Waiting for client to connect...");

        try (
            ServerSocket serv = new ServerSocket(port);
            Socket sock = serv.accept();
            OutputStream out = sock.getOutputStream();
            InputStream in = sock.getInputStream()
        ) {

            System.out.println("New client has connected on port: " + port);

            while (true) {
                byte[] requestBytes = NetworkUtils.Receive(in);
                JSONObject request = JsonUtils.fromByteArray(requestBytes);

                System.out.println("Received:\n" + request);

                JSONObject response = makeResponse(request);

                byte[] output = JsonUtils.toByteArray(response);
                NetworkUtils.Send(out, output);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONObject makeResponse(JSONObject request) {
        switch (request.getString("type")) {
            case "echo" -> {
                return createEchoResponse(request.getString("body"));
            }
            case "reverse" -> {
                return createReverseResponse(request.getString("body"));
            }
        }
        return createErrorResponse();
    }

    public static JSONObject createEchoResponse(String message) {
        JSONObject response = new JSONObject();
        response.put("type", "echo");
        response.put("body", message);
        return response;
    }

    public static JSONObject createReverseResponse(String message) {
        JSONObject response = new JSONObject();
        response.put("type", "reverse");
        StringBuilder reversedMessage = new StringBuilder(message);
        reversedMessage.reverse();
        response.put("body", reversedMessage);
        return response;
    }

    public static JSONObject createErrorResponse() {
        JSONObject response = new JSONObject();
        response.put("type", "error");
        response.put("body", "Unable to process your request at this time. Please try again.");
        return response;
    }


}
