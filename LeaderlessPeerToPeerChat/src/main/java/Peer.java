import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import java.io.PrintWriter;

/**
 * This is the main class for the peer2peer program.
 * It starts a client with a username and host:port for the peer and host:port of the initial leader
 * This Peer is basically the client of the application, while the server (the one listening and waiting for requests)
 * is in a separate thread ServerThread
 * In here you should handle the user input and then send it to the server of annother peer or anything that needs to be done on the client side
 * YOU CAN MAKE ANY CHANGES YOU LIKE: this is a very basic implementation you can use to get started
 *
 */

public class Peer {
	private final String username;
	private final BufferedReader bufferedReader;
	private final ServerThread serverThread;

	private final Set<SocketInfo> peers = new HashSet<SocketInfo>();
	private final SocketInfo mySocket;

	public Peer(BufferedReader bufReader, String username,ServerThread serverThread, SocketInfo mySocket){
		this.username = username;
		this.bufferedReader = bufReader;
		this.serverThread = serverThread;
		this.mySocket = mySocket;
	}

	public void addPeer(SocketInfo si){
		for (SocketInfo s : peers) {
			if(Objects.equals(s.host(), si.host()) && s.port() == si.port()){
				return;
			}
		}
		peers.add(si);
	}

	// get a string of all peers that this peer knows
	public String getPeers(){
		StringBuilder s = new StringBuilder();
		for (SocketInfo p: peers){
			s.append(this.username).append(":").append(p.host()).append(":").append(p.port()).append(" ");
		}
		return s.toString();
	}

	/**
	 * Adds all the peers in the list to the peers list
	 * Only adds it if it is not the currect peer (self)
	 *
	 * @param list String of peers in the format "host1:port1 host2:port2"
	 */
	public void updateListenToPeers(String list) throws Exception {
		String[] peerList = list.split(" ");
		for (String p: peerList){
			String[] hostPort = p.split(":");

			// basic check to not add yourself, since then we would send every message to ourself as well (but maybe you want that, then you can remove this)
			if ((hostPort[0].equals("localhost") || hostPort[0].equals(serverThread.getHost())) && Integer.parseInt(hostPort[1]) == serverThread.getPort()){
				continue;
			}
			SocketInfo s = new SocketInfo(hostPort[0], Integer.parseInt(hostPort[1]));
			addPeer(s);
		}
	}

	/**
	 * Client waits for user to input can either exit or send a message
	 */
	public void askForInput() throws Exception {
		try {

			System.out.println("> You can now start chatting (exit to exit)");
			while(true) {
				String message = bufferedReader.readLine();
				if (message.equals("exit")) {
					System.out.println("bye, see you next time");
					break;
				} else {
					sendMessageToAll("{'type': 'message', 'username': '"+ username +"','message':'" + message + "'}");
				}
			}
			System.exit(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean sendMessage(String message, SocketInfo peer) {
		boolean sent = false;
		try {
			System.out.println("     Trying to reach a peer in the network: ");
			Socket socket = null;
			try {
				socket = new Socket(peer.host(), peer.port());
			} catch (Exception c) {
				System.out.println("  Could not connect to " + peer.host() + ":" + peer.port());
				System.out.println("  Removing that socketInfo from list");
				System.out.println("     Issue: " + c);
				peers.remove(peer);
			}

			if (peer != mySocket) {
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				out.println(message);
				socket.close();
				System.out.println("     Message was sent to a peer");
				sent = true;
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
		return sent;
	}

	/**
	 * Send a message to every peer in the peers list, if a peer cannot be reached remove it from list
	 *
	 * @param message String that peer wants to send to other peers
	 */
	public void sendMessageToAll(String message) {
		try {
			System.out.println("     Trying to send to peers: " + (peers.size() - 1));
			int counter = 0;
			for (SocketInfo s : peers) {
				if (sendMessage(message, s)) {
					counter++;
				}
			}
			System.out.println("     Message was sent to " + counter + " peers");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Main method saying hi and also starting the Server thread where other peers can subscribe to listen
	 *
	 * @param args[0] username
	 * @param args[1] port for server
	 */
	public static void main (String[] args) throws Exception {

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String username = args[0];
		System.out.println("Hello " + username + " and welcome! Your port will be " + args[1]);

		int size = args.length;
		System.out.println(size);
		if (!Objects.equals(args[2], "no-target")) {
			System.out.println("Started peer");
			System.out.println(args[0] + " " + args[1]);
			ServerThread serverThread = new ServerThread(args[1]);
			String[] myHostPort = args[1].split(":");
			SocketInfo mySocket = new SocketInfo(myHostPort[0], Integer.parseInt(myHostPort[1]));
			Peer peer = new Peer(bufferedReader, username, serverThread, mySocket);
			peer.addPeer(mySocket);

			String[] hostPort = args[2].split(":");
			SocketInfo s = new SocketInfo(hostPort[0], Integer.parseInt(hostPort[1]));

			// add peer to list
			peer.addPeer(s);

			// send message to leader that we want to join
			peer.sendMessage("{'type': 'join', 'username': '"+ username +"','ip':'" + serverThread.getHost() + "','port':'" + serverThread.getPort() + "'}", s);

			serverThread.setPeer(peer);
			serverThread.start();
			peer.askForInput();
		} else if (args[2].equalsIgnoreCase("no-target")){
			System.out.println("Started first peer");
			System.out.println(args[0] + " " + args[1]);
			ServerThread serverThread = new ServerThread(args[1]);
			String[] myHostPort = args[1].split(":");
			SocketInfo mySocket = new SocketInfo(myHostPort[0], Integer.parseInt(myHostPort[1]));
			Peer peer = new Peer(bufferedReader, username, serverThread, mySocket);
			peer.addPeer(mySocket);

			serverThread.setPeer(peer);
			serverThread.start();
			peer.askForInput();
		}else {
			System.out.println("Expected: <name(String)> <peer(String)> <target(String)>");
			System.exit(0);
		}



	}

}