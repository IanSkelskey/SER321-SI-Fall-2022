# Living Client-Server

## Purpose:

The living client-server application is a pair programming exercise. 
The intent is for it to be updated throughout SER321 as new concepts are learned.

### Progression

1. Create bare minimum TCP/UDP client-server applications.
    These programs should establish a connection only. Further 
    functionality will be added throughout the remainder of the
    semester.
    - TCP requires a `Socket` and a `ServerSocket`
    - UDP requires `DatagramSockets` and `DatagramPackets`
      - The string class comes with a `getBytes()` method to convert a String to bytes.
      - You will need to write a method to convert bytes to String.
2. Implement simple message passing between client and server.
   - Having the server echo the client's messages is an easy option.
3. Implement multithreading.
4. Replace message passing with protobuf.