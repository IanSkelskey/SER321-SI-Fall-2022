package examples.grpcclient;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;
import service.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

// just to show how to use the empty in the protobuf protocol
    // Empty empt = Empty.newBuilder().build();

/**
 * Client that requests `parrot` method from the `EchoServer`.
 */
public class EchoClient {
  private final EchoGrpc.EchoBlockingStub echoStub;
  private final JokeGrpc.JokeBlockingStub jokeStub;
  private final RockPaperScissorsGrpc.RockPaperScissorsBlockingStub rpsStub;
  private final BinaryGrpc.BinaryBlockingStub binaryStub;
  private final RegistryGrpc.RegistryBlockingStub registryStub;

  /** Construct client for accessing server using the existing channel. */
  public EchoClient(Channel channel, Channel regChannel) {
    // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's
    // responsibility to
    // shut it down.
    // Passing Channels to code makes code easier to test and makes it easier to
    // reuse Channels.
    echoStub = EchoGrpc.newBlockingStub(channel);
    jokeStub = JokeGrpc.newBlockingStub(channel);
    rpsStub = RockPaperScissorsGrpc.newBlockingStub(channel);
    binaryStub = BinaryGrpc.newBlockingStub(channel);
    registryStub = RegistryGrpc.newBlockingStub(regChannel);
  }

  public void askToPlayRockPaperScissors(String name, int play) {
    PlayReq request = PlayReq.newBuilder()
            .setName(name)
            .setPlay(PlayReq.Played.forNumber(play))
            .build();
    PlayRes response;

    try {
      response = rpsStub.play(request);
      System.out.println(response.getMessage());
      // System.out.println(response.getError());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public void askForLeaderboard() {
    com.google.protobuf.Empty empty = com.google.protobuf.Empty.newBuilder().build();
    try {
      LeaderboardRes response = rpsStub.leaderboard(empty);
      System.out.println("\nRock Paper Scissors Leaderboard");
      System.out.println("Rank\tName\tWins\tLosses");
      for (LeaderboardEntry e: response.getLeaderboardList()) {
        System.out.print(e.getRank());
        System.out.print("\t" + e.getName());
        System.out.print("\t" + e.getWins());
        System.out.println("\t" + e.getLost());
      }
      System.out.println(response.getError());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public void askServerToParrot(String message) {
    ClientRequest request = ClientRequest.newBuilder()
            .setMessage(message)
            .build();
    ServerResponse response;
    try {
      response = echoStub.parrot(request);
    } catch (Exception e) {
      System.err.println("RPC failed: " + e.getMessage());
      return;
    }
    System.out.println("Received from server: " + response.getMessage());
  }

  public void askToConvertBinaryToString(String input) {
    BinaryToStringReq request = BinaryToStringReq.newBuilder()
            .setBinary(input)
            .build();
    ConversionResponse response;
    try {
      response = binaryStub.sendBinary(request);
      System.out.println(response.getResult());
      System.out.println(response.getError());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
    }
  }

  public void askToConvertStringToBinary(String input) {
    StringToBinaryReq request = StringToBinaryReq.newBuilder()
            .setStr(input)
            .build();
    ConversionResponse response;
    try {
      response = binaryStub.sendString(request);
      System.out.println(response.getResult());
      System.out.println(response.getError());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
    }
  }

  public void askForJokes(int num) {
    JokeReq request = JokeReq.newBuilder().setNumber(num).build();
    JokeRes response;
    try {
      response = jokeStub.getJoke(request);
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
    System.out.println("Your jokes: ");
    for (String joke : response.getJokeList()) {
      System.out.println("--- " + joke);
    }
  }

  public void setJoke(String joke) {
    JokeSetReq request = JokeSetReq.newBuilder().setJoke(joke).build();
    JokeSetRes response;

    try {
      response = jokeStub.setJoke(request);
      System.out.println(response.getOk());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
    }
  }

  public void getServices() {
    GetServicesReq request = GetServicesReq.newBuilder().build();
    ServicesListRes response;
    try {
      response = registryStub.getServices(request);
      System.out.println(response.toString());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
    }
  }

  public void findServer(String name) {
    FindServerReq request = FindServerReq.newBuilder().setServiceName(name).build();
    SingleServerRes response;
    try {
      response = registryStub.findServer(request);
      System.out.println(response.toString());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public void findServers(String name) {
    FindServersReq request = FindServersReq.newBuilder().setServiceName(name).build();
    ServerListRes response;
    try {
      response = registryStub.findServers(request);
      System.out.println(response.toString());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 6) {
      System.out
          .println("Expected arguments: <host(String)> <port(int)> <regHost(string)> <regPort(int)> <message(String)> <regOn(bool)>");
      System.exit(1);
    }
    int port = 9099;
    int regPort = 9003;
    String host = args[0];
    String regHost = args[2];
    String message = args[4];
    try {
      port = Integer.parseInt(args[1]);
      regPort = Integer.parseInt(args[3]);
    } catch (NumberFormatException nfe) {
      System.out.println("[Port] must be an integer");
      System.exit(2);
    }

    // Create a communication channel to the server, known as a Channel. Channels
    // are thread-safe
    // and reusable. It is common to create channels at the beginning of your
    // application and reuse
    // them until the application shuts down.
    String target = host + ":" + port;
    ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS
        // to avoid
        // needing certificates.
        .usePlaintext().build();

    String regTarget = regHost + ":" + regPort;
    ManagedChannel regChannel = ManagedChannelBuilder.forTarget(regTarget).usePlaintext().build();
    try {

      // ##############################################################################
      // ## Assume we know the port here from the service node it is basically set through Gradle
      // here.
      // In your version you should first contact the registry to check which services
      // are available and what the port
      // etc is.

      /**
       * Your client should start off with 
       * 1. contacting the Registry to check for the available services
       * 2. List the services in the terminal and the client can
       *    choose one (preferably through numbering) 
       * 3. Based on what the client chooses
       *    the terminal should ask for input, eg. a new sentence, a sorting array or
       *    whatever the request needs 
       * 4. The request should be sent to one of the
       *    available services (client should call the registry again and ask for a
       *    Server providing the chosen service) should send the request to this service and
       *    return the response in a good way to the client
       * 
       * You should make sure your client does not crash in case the service node
       * crashes or went offline.
       */

      // Just doing some hard coded calls to the service node without using the
      // registry
      // create client
      EchoClient client = new EchoClient(channel, regChannel);

      // call the parrot service on the server
      client.askServerToParrot(message);

      // ask the user for input how many jokes the user wants
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

      // adding a joke to the server
      client.setJoke("I made a pencil with two erasers. It was pointless.");

      // showing 6 jokes
      client.askForJokes(6);

      // Convert String to binary
      client.askToConvertStringToBinary("Hello");

      // Convert binary to string
      client.askToConvertBinaryToString("0100100001100101011011000110110001101111");

      client.askToPlayRockPaperScissors("Ian", 0);
      client.askToPlayRockPaperScissors("John", 2);
      client.askToPlayRockPaperScissors("Ian", 1);
      client.askToPlayRockPaperScissors("John", 0);
      client.askToPlayRockPaperScissors("Ian", 0);
      client.askToPlayRockPaperScissors("John", 2);
      client.askToPlayRockPaperScissors("Ian", 1);
      client.askToPlayRockPaperScissors("John", 0);
      client.askForLeaderboard();


      // ############### Contacting the registry just so you see how it can be done

      if (args[5].equals("true")) { 
        // Comment these last Service calls while in Activity 1 Task 1, they are not needed and wil throw issues without the Registry running
        // get thread's services
        client.getServices(); // get all registered services 

        // get parrot
        client.findServer("services.Echo/parrot"); // get ONE server that provides the parrot service
        
        // get all setJoke
        client.findServers("services.Joke/setJoke"); // get ALL servers that provide the setJoke service

        // get getJoke
        client.findServer("services.Joke/getJoke"); // get ALL servers that provide the getJoke service

        System.out.println("Servers with binary service: ");

        // convert binary to string
        client.findServer("services.Binary/sendBinary");

        // convert binary to string
        client.findServer("services.Binary/sendString");

        // does not exist
        client.findServer("random"); // shows the output if the server does not find a given service
      }

    } finally {
      // ManagedChannels use resources like threads and TCP connections. To prevent
      // leaking these
      // resources the channel should be shut down when it will no longer be used. If
      // it may be used
      // again leave it running.
      channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
      regChannel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }
  }
}
