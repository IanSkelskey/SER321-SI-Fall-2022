#### Purpose:
Demonstrate simple Client and Server communication using `SocketServer` and `Socket`classes.

Here a simple protocol is defined which uses protobuf. The client reads in a json file and then creates a protobuf object from it to send it to the server. The server reads it and sends back the calculated result. 

The response is also a protobuf but only with a result string. 

To see the proto file see: src/main/proto which is the default location for proto files. 

Gradle is already setup to compile the proto files. 

### The procotol
See the PROTOCOL.md for details.


### How to run it (optional)
The proto file can be compiled using

``gradle generateProto``

This will also be done when building the project. 

You should see the compiled proto file in Java under build/generated/source/proto/main/java/buffers

Now you can run the client and server 

#### Default 
Server is Java
Per default on 9099
runServer

You have one example client in Java using the Protobuf protocol

Clients runs per default on 
host localhost, port 9099
Run Java:
	runClient


#### With parameters:
Java
gradle runClient -Pport=9099 -Phost='localhost'
gradle runServer -Pport=9099