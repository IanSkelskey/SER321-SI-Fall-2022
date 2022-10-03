import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintWriter;

import org.json.*;

/**
 * This is the class that handles communication with a peer/client that has connected to use
 * and wants something from us
 *
 */

public class ServerTask extends Thread {
	private final BufferedReader bufferedReader;
	private Peer peer = null; // so we have access to the peer that belongs to that thread
	private PrintWriter out = null;
	private Socket socket = null;

	// Init with socket that is opened and the peer
	public ServerTask(Socket socket, Peer peer) throws IOException {
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		this.peer = peer;
		this.socket = socket;
	}

	// basically wait for an input, right now we can only handle a join request
	// and a message
	// More requests will be needed to make everything work
	// You can enhance this or totally change it, up to you. 
	// I used simple JSON here, you can use your own protocol, use protobuf, anything you want
	// in here this is not done especially pretty, I just use a PrintWriter and BufferedReader for simplicity
	public void run() {
		while (true) {
			try {
				JSONObject json = new JSONObject(bufferedReader.readLine());

				if (json.getString("type").equals("join")){
					System.out.println("     " + json.getString("username") + " wants to join the network");
					peer.updateListenToPeers(json.getString("ip") + ":" + json.getInt("port"));
					peer.sendMessageToAll("{'type': 'update-list', 'list': '"+ peer.getPeers() +"'}");
				} else if (json.getString("type").equals("update-list")){
					String peerList = json.getString("list");
					String[] peerArray = peerList.split(" ");
					for (String p : peerArray){
						String[] id = p.split(":");
						// String username = id[0];
						String host = id[1];
						int port = Integer.parseInt(id[2]);
						peer.updateListenToPeers(host + ":" + port);
					}
				}else {
					System.out.println("[" + json.getString("username")+"]: " + json.getString("message"));
				}


			} catch (Exception e) {
				interrupt();
				break;
			}
		}
	}
}