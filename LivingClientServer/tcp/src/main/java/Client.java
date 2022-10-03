import buffers.EchoProto;

import java.io.*;
import java.net.*;

/**
 * Created Referencing:
 * <a href="https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/networking/sockets/examples/EchoClient.java">Oracle EchoClient.java</a>
 */
class Client {

    public static void main(String[] args) throws Exception {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        try (
                Socket sock = new Socket(host, port);
                OutputStream out = sock.getOutputStream();
                InputStream in = sock.getInputStream();
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Host: " + host + "\nPort: " + sock.getPort());
            String message;
            printMenu();
            while ((message = stdIn.readLine()) != null) {
                EchoProto.Type type = EchoProto.Type.ECHO;
                switch(message) {
                    case "1":
                        System.out.println("Enter a message for the server to echo!\n");
                        message = stdIn.readLine();
                        break;
                    case "2":
                        type = EchoProto.Type.REVERSE;
                        System.out.println("Enter a message for the server to reverse!\n");
                        message = stdIn.readLine();
                        break;
                    case "0":
                        type = EchoProto.Type.EXIT;
                        message = "exit";
                        break;
                    default:
                        System.out.println("Wrong type. Try again.");
                        break;
                }
                EchoProto.Echo echoOut = EchoProto.Echo.newBuilder()
                        .setMessage(message)
                        .setType(type)
                        .build();
                echoOut.writeDelimitedTo(out);

                if (type == EchoProto.Type.EXIT) break;

                EchoProto.Echo echoIn = EchoProto.Echo.parseDelimitedFrom(in);
                String receivedMessage = echoIn.getMessage();
                System.out.println("Server: " + receivedMessage);
                printMenu();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printMenu() {
        System.out.println("Choose an echo service!");
        System.out.println("1: Echo - Server will echo a string.");
        System.out.println("2: Reverse - Server will reverse a string.");
        System.out.println("0: Exit - Quit the program");
    }

}
