import echo.ClientRequest;
import echo.EchoGrpc;
import echo.ServerResponse;
import io.grpc.stub.StreamObserver;

class EchoImpl extends EchoGrpc.EchoImplBase {

    @Override
    public void parrot(ClientRequest request, StreamObserver<ServerResponse> responseObserver) {
        System.out.println("Preparing to parrot: " + request.getMessage());
        ServerResponse response = ServerResponse.newBuilder()
                .setMessage(request.getMessage())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void reverse(ClientRequest request, StreamObserver<ServerResponse> responseObserver) {
        System.out.println("Preparing to reverse: " + request.getMessage());
        String reversed = String.valueOf(new StringBuilder(request.getMessage()).reverse());
        ClientRequest newRequest = request.toBuilder().setMessage(reversed).build();
        parrot(newRequest, responseObserver);
    }
}