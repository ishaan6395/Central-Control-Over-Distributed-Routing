Information related to the project:
- This project demonstrates fibbing over a network topology with 8 routers.
- It uses OSPF protocol which is a link state routing protocol.
- We simulate fibbing in the following two cases: 
	- When a link fails
	- When a link is in congestion
- We simulate the whole network topology and fibbing algorithm over 8 routers using Docker.

Steps to run the program:

- clone the project using command "git clone https://github.com/ishaan6395/Routing_Information_Protocol.git"

- create a docker image in the folder using command ./create.sh

- Open 9 terminals.

- In one terminal type command "docker run --rm -it -e SYSTEM_TYPE=server fibbing"

- In rest eight terminals type command "docker run --rm -it -e SYSTEM_TYPE=Client fibbing"

- Once all the routers have the picture of the whole topology, in the first Client terminal type in the command "start simulation". As according to the forwarding requirement first terminal is the source.

