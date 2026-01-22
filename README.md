## Truck Robot REST API

This application controls a virtual truck robot moving on a bounded table.
Commands are submitted over HTTP via a REST API to control the robot.

The robot begins in an unplaced (missing) state. While unplaced, movement and rotation commands are ignored.

The project is written in Java 21 and maven

### Supported Commands
PLACE x y NORTH|EAST|SOUTH|WEST
Places the robot at the given coordinates and facing direction.
Invalid placements (out of bounds or null direction) are ignored.

FORWARD
Moves the robot one unit forward in the direction it is currently facing.
Moves that would cause the robot to fall off the table are ignored.

TURN LEFT|RIGHT
Rotates the robot 90 degrees left or right.

REPORT
Outputs the robot’s current position and facing direction, or
ROBOT MISSING if the robot has not yet been placed.

### API Endpoints (v1):
/api/v1/command
Execute a single command.
If a command contains arguments, they are separated by a single space character.

Example:
"PLACE 1 1 NORTH"

Example usage using curl through cmd.exe:
curl.exe -X POST http://localhost:8080/api/v1/command -H "Content-Type: text/plain" --data "PLACE 1 1 NORTH"
curl.exe -X POST http://localhost:8080/api/v1/command -H "Content-Type: text/plain" --data "REPORT"
"PLACE 1 1 NORTH"

/api/v1/commands
Execute a batch of commands.
Each command is separated by a comma.
This is equivalent to sending individual commands to /api/v1/command

Example:
"PLACE 1 1 NORTH,FORWARD,REPORT"

Example usage using curl:
curl.exe -X POST http://localhost:8080/api/v1/commands -H "Content-Type: text/plain" --data "PLACE 1 1 NORTH,FORWARD,TURN LEFT,REPORT"
1,2,WEST

### JSON API:

JSON API is preferred for production quality REST API purposes. It is currently a wrapper over the plain text API. 

Responses are objects keyed with 'output'

/api/v1/command
keyed with "command"
curl.exe -X POST http://localhost:8080/api/v1/command -H "Content-Type: application/json" --data "{\"command\": \"REPORT\"}"
{"output":"ROBOT MISSING"}

/api/v1/commands
keyed with "commands"
curl.exe -X POST http://localhost:8080/api/v1/commands -H "Content-Type: application/json" --data "{\"commands\": [\"PLACE 3 3 WEST\",\"REPORT\"]}"
{"output":"3,3,WEST"}

The JSON API may be a little clunkier to use in cmd.exe than the text/plain API due to backslashes in the data payload.
It may be easier to use e.g a browser client's Network Devtools.

## Deployment

To start the REST API server locally:

mvn clean test
mvn spring-boot:run