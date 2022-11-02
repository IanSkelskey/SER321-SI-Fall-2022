import org.json.JSONObject;
import utils.JsonUtils;
import utils.NetworkUtils;

import java.io.*;
import java.net.Socket;

class Client {

    private static final BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try (
             Socket sock = new Socket(host, port);
             OutputStream out = sock.getOutputStream();
             InputStream in = sock.getInputStream()
        ) {
            System.out.println("Host: " + host + "\nPort: " + port);
            while (true) {
                showMenu();
                String selection = stdIn.readLine();
                JSONObject request = makeRequest(Integer.parseInt(selection));

                if (request != null) {
                    NetworkUtils.Send(out, JsonUtils.toByteArray(request));

                    byte[] responseBytes = NetworkUtils.Receive(in);
                    JSONObject response = JsonUtils.fromByteArray(responseBytes);
                    System.out.println("Response: " + response.get("body"));
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public static void showMenu() {
        System.out.println("Please choose a service from the following list.");
        System.out.println("1\tEcho");
        System.out.println("2\tReverse");
        System.out.println("0\tExit");
    }

    public static JSONObject makeRequest(int selection) {
        switch (selection) {
            case 1 -> {
                System.out.print("Please enter a message for the server to echo: ");
                try {
                    String message = stdIn.readLine();
                    return createEchoRequest(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case 2 -> {
                System.out.print("Please enter a message for the server to reverse: ");
                try {
                    String message = stdIn.readLine();
                    return createReverseRequest(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case 0 -> System.exit(0);
            default -> System.out.println("Invalid selection. Please try again.");
        }
        return null;
    }

    public static JSONObject createEchoRequest(String message) {
        JSONObject request = new JSONObject();
        request.put("type", "echo");
        request.put("body", message);
        return request;
    }

    public static JSONObject createReverseRequest(String message) {
        JSONObject request = new JSONObject();
        request.put("type", "reverse");
        request.put("body", message);
        return request;
    }

}