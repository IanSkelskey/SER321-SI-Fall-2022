import io.grpc.stub.StreamObserver;
import si.EchoGrpc;
import si.ClientRequest;
import si.ServerResponse;

public class EchoImpl extends EchoGrpc.EchoImplBase {

    @Override
    public void parrot(ClientRequest request, StreamObserver<ServerResponse> responseObserver) {
        System.out.println("Received request to echo: " + request.getMessage());
        ServerResponse response = ServerResponse.newBuilder()
                .setMessage(request.getMessage())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
