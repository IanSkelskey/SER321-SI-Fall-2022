import io.grpc.stub.StreamObserver;
import si.EchoGrpc;
import si.ClientRequest;
import si.ServerResponse;

public class EchoImpl extends EchoGrpc.EchoImplBase {

    @Override
    public void parrot(ClientRequest request, StreamObserver<ServerResponse> responseObserver) {

    }

}
