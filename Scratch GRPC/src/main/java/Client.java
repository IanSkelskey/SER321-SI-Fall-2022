import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import si.EchoGrpc.EchoBlockingStub;
import si.ClientRequest;
import si.ServerResponse;

import java.util.concurrent.TimeUnit;

import static si.EchoGrpc.newBlockingStub;

public class Client {
    private final EchoBlockingStub echoBlockingStub;

    public Client(Channel channel) {
        this.echoBlockingStub = newBlockingStub(channel);
    }

    public static void main(String[] args) {
        System.out.println("Client is running...");
        String host = args[0];
        int port = 8088;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.exit(1);
        }

        String endPoint = host + ":" + port;

        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(endPoint)
                .usePlaintext()
                .build();

        Client client = new Client(channel);

        ClientRequest req = ClientRequest.newBuilder()
                .setMessage("Hello")
                .build();

        ServerResponse res = client.echoBlockingStub.parrot(req);

        System.out.println(res.getMessage());

        try {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
