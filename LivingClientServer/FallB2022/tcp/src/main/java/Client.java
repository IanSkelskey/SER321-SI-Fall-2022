import org.json.JSONArray;
import org.json.JSONObject;
import utils.JsonUtils;
import utils.NetworkUtils;

import java.io.*;
import java.net.Socket;

import static utils.Protocol.*;

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

                if (request == null) {
                    System.out.println("Failed to construct request. Please try again.");
                    continue;
                }

                NetworkUtils.Send(out, JsonUtils.toByteArray(request));

                if (request.getString("type").equalsIgnoreCase("exit")) {
                    System.exit(0);
                }

                byte[] responseBytes = NetworkUtils.Receive(in);
                JSONObject response = JsonUtils.fromByteArray(responseBytes);
                switch (response.getString("type")) {
                    case "string array" -> {
                        JSONArray array = response.getJSONArray("body");
                        for (int i = 0; i < array.length(); i++) {
                            System.out.println(array.getString(i));
                        }
                    }
                    default -> System.out.println("Response: " + response.get("body"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showMenu() {
        System.out.println("Please choose a service from the following list.");
        System.out.println("1\tEcho");
        System.out.println("2\tReverse");
        System.out.println("3\tString Array");
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
            case 3 -> {
                return createStringArrayRequest();
            }
            case 0 -> {
                return createExitRequest();
            }
            default -> System.out.println("Invalid selection. Please try again.");
        }
        return null;
    }



}