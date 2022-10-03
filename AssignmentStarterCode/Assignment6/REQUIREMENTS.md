# Assignment 6 Requirements

The submission should include a build.gradle file, a README, and the necessary source files.

## README
- [ ] A description of the project and a detailed description of what it does and which requirements it fulfills.
- [ ] An explanation of how to run the program. Include the gradle command.
- [ ] Explanation of how to work with the program. What inputs does it expect? etc.
- [ ] A link to a screencast.

## Task 1: Starting Services Locally (55 points)

- [ ] The service node must run with `gradle runNode -pRegOn=false`.
- [ ] The client must run with `gradle runClient -pRegOn=false`.
  - [ ] Must use the correct default values to connect to the started service node.
- [ ] Implement two of the following proto files: (17.5 points each)
  - [ ] Zodiac
  - [ ] Woof
  - [ ] Recipe
  - [ ] Hometowns
- [ ] The client should let the user decide what they want to do with clean terminal output. (8 points)
  - [ ] The client should list all available services.
  - [ ] The client should ask the user which service they would like to use.
  - [ ] The client will wait for user's selection.
  - [ ] The client will then ask for the input necessary for the selected service.
  - [ ] The client does not crash.
- [ ] There is an option to run `gradle runClient -Pauto=1` which will run all requests on its own. (8 points)
  - [ ] Input data for all requests is hard coded.
  - [ ] Show outputs and which services were called.
  - [ ] Call the server directly without using any registry.
- [ ] Server and client are robust and do not crash. (4 points)

## Task 2: Inventing New Service (30 points)

Create your own proto file with 0 - 2 of your peers. This proto file will offer a new service that the program does not already support. The implementation must be written on your own.

- [ ] Protocol design (10 points)
- [ ] Client (10 points)
- [ ] Server (10 points)
  - [ ] The service allows at least two different requests.
  - [ ] Each request needs at least one input.
  - [ ] Response returns different data for different requests.
  - [ ] Response returns a repeated field.

## Task 3: Building a Network Together (18 points)

Now we want to create a network of nodes and register them so others can access your services.

### Task 3.1: Register Things Locally (10 points)

1. If you run your node and client with `-pRegOn=true` and run your registry before starting the node, your node should register on the registry.

2. Test this: run the registry and run a node. You should see a print out on the registry side that the service is registered.

3. Now, you should run your client and see if it will find the registered services correctly.

4. Adapt the client so it does not just call the service on the node you provide directly as was done in task 1, but that the client can choose between all services registered on the registry that you can support on the client side. (You do not have to show services that you did not implement)

   For testing purposes you can run a couple server nodes and register all of them to your local registry. You do not hard code which server to talk to anymore, but use the following workflow.

   1. Client contacts registry to check for available services (this is shown already in the client class)
   2. Show all services that are available and the host:port that they are on (preferably through numbering)
   3. Based on what the client chooses the terminal should ask for the needed input.
   4. The request should be sent to the chosen service nodes. For example parrot is implemented on 2 nodes which showed up in the list with number 1 and 4. User chose number 4. Now you read the host and port from number 4 and connect to that specific node, to then call the parrot service.
   5. Make sure that your client does not crash in case the server did not respond or crashed. Make it as robust as possible.

### Task 3.2: ReadMe/Screencast (5 points)

### Task 3.3: Slack (3 points EC)

Post your server on slack with the exact command on how to reach it (port and ip) and also which services you implemented. Others can try to connect to it and try out their own clients.