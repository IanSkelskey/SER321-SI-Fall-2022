import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client {

    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket sock = new Socket(host, port);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
             PrintWriter out = new PrintWriter(sock.getOutputStream()))
        {
            System.out.println("Host: " + host + "\nPort: " + port);
            System.out.println("Enter a message for the server to echo!\n");
            String userInput = stdIn.readLine();
            System.out.println("You entered: " + userInput);
            out.println(userInput);
            out.flush();
            System.out.println(in.readLine());
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}