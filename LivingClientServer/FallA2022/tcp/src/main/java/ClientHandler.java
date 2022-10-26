import buffers.EchoProto;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class ClientHandler implements Runnable {

    private static int count = 0;
    private final Socket socket;
    private final OutputStream out;
    private final InputStream in;

    public ClientHandler(ServerSocket server) throws IOException {
        this.socket = server.accept();
        out = socket.getOutputStream();
        in = socket.getInputStream();
    }

    @Override
    public void run() {
        incrementCount();
        System.out.println("New client has connected!");
        printCount();
        try {
            EchoProto.Echo echoIn = EchoProto.Echo.parseDelimitedFrom(in);
            String receivedMessage = echoIn.getMessage();
            EchoProto.Type type = echoIn.getType();
            while (type != EchoProto.Type.EXIT) {
                System.out.println("Received: " + receivedMessage);
                if (type == EchoProto.Type.REVERSE) {
                    receivedMessage = reverse(receivedMessage);
                }
                EchoProto.Echo echoOut = EchoProto.Echo.newBuilder()
                        .setMessage(receivedMessage)
                        .setType(type)
                        .build();
                echoOut.writeDelimitedTo(out);
                // Get Response
                echoIn = EchoProto.Echo.parseDelimitedFrom(in);
                receivedMessage = echoIn.getMessage();
                type = echoIn.getType();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    private String reverse(String message) {
        StringBuilder reversed = new StringBuilder(message).reverse();
        return reversed.toString();
    }

    public synchronized static int getCount() {
        return count;
    }

    public synchronized static void incrementCount() {
        count++;
    }

    public synchronized static void decrementCount() {
        count--;
    }

    public static void printCount() {
        System.out.println("Total number of clients is now: " + getCount());
    }

    private void disconnect() {
        try {
            this.socket.close();
            System.out.println("Client has disconnected!");
            decrementCount();
            printCount();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}