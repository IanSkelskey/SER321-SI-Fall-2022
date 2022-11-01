import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);

        try (ServerSocket serv = new ServerSocket(port);
             Socket sock = serv.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
             PrintWriter out = new PrintWriter(sock.getOutputStream(), true))
        {
            System.out.println("New client has connected on port: " + port);
            String incoming = in.readLine();
            System.out.println("Received: " + incoming);
            out.println("Echo: " + incoming);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
