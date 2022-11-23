import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ServiceProvider {
    private Server server;
    private int port;

    ServiceProvider(int port) {
        this.port = port;
    }

    private void start() throws IOException {
        server = ServerBuilder.forPort(this.port)
                .addService(new EchoImpl())
                .build()
                .start();

        System.out.println("Server is now running...");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Shutting down GRPC Server");
            // TODO: Stop the GRPC Server
            try {
                this.stop();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.err.println("Server has been shut down.");
        }));
    }

    private void stop() throws InterruptedException {
        if (server == null) {
            return;
        }
        server.shutdown().awaitTermination();
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server == null) {
            return;
        }
        server.awaitTermination();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 8088;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.exit(1);
        }

        final ServiceProvider server = new ServiceProvider(port);
        server.start();
        server.blockUntilShutdown();
    }

}
