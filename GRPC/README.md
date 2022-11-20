# GRPC Example

### Purpose

This example shows how you can use gRPC to have a client and server communiate using Protobuf as a protocol.
You have a given Python server/client and Java server/client. All of them can communicate with each other.

## Java

You do not need to install anything things will run through the gradle file.

- `gradle runServerJava -q --console=plain`
- `gradle runClientJava -q --console=plain`

`host`, `port` and `message` are optional arguments for the program.

The below is only needed for Python which is nothing we need for the course

## PYTHON

### Install Dependencies

These need to be installed

###### (use of virtualenv recommended for `pip` installs)

1. install protoc
2. pip install protobuf
3. pip install grpcio
4. pip install grpcio-tools

##### To compile grpc and protocol buffers for Gradle [from the `Sockets` directory]:

- `gradle generateProto -q --console=plain`
- `gradle pythonProto -q --console=plain`

This will generate the py files for proto and grpc, sorry for the two separate calls.

#### Python (Install the dependencies before running these)

- `gradle runServerPython -q --console=plain`
- `gradle runClientPython -q --console=plain`

`host`, `port` and `message` are optional arguments for the program.e
