# Leaderless Peer to Peer Chat

## Requirements:
- [x] When a new node is started it should contact one of the already existing nodes (any node that is already in the network not the designated one)
- [x] The host and port of one already running node is passed in the Gradle task when starting a new node.
- [x] There is no leader node.
- [x] README.md explains which code is used and how the goal is accomplished
- [x] [Screencast](https://youtu.be/EYy9B0mqscc)

## Purpose:
Very basic peer-2-peer for a chat. All peers can communicate with each other. 

Each peer is client and server at the same time. 
When started the peer has a ServerThread in which the peer listens for potential other peers to connect.

You can join the chat by contacting any node that is already in the network.

## How to Run
> ### Start New Chat
> If you are starting a new chat room, then you simply do not specify a target node to contact. You only
> need to specify a name for yourself as well as your host and port
> The program will start and wait for incoming connections. You may still type messages before others enter chat,
> but no one will receive them.
> 
> The command for this is:
> `gradle runPeer -PpeerName=Ian -Ppeer="localhost:8080" -q --console=plain`

> ### Join Existing Chat
> If you are joining an existing chat room, you will need to specify the information of the in-network node
> that you are trying to contact.
> 
> The command for this is:
> `gradle runPeer -PpeerName=Niko -Ppeer="localhost:7000" -Ptarget="localhost:8080" -q --console=plain`

## Explanation
I used the Peer2Peer example from class as a base for this activity. I first removed all references to the
original leader structure so that all peers would try to notify others of new arrivals. Then I added a message type
encoded with JSON, "update-list". When a new peer sends a "join" message to an in-network node, that node 
then sends a message to all the nodes that it knows to update their lists. Nodes' lists now include their own
identifying information as well. The `sendMessageToAll(message)` method is updated to ensure that nodes do not
message themselves. The `addPeer(si)` method now checks to make sure that the new peer is not already in the list
before adding it.
