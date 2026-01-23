## Truck Robot REST API

This application controls a virtual truck robot moving on a bounded table. This is a single global robot.
Commands are submitted over HTTP via a REST API to control the robot.

The robot begins in an unplaced (missing) state. While unplaced, movement and rotation commands are ignored.

The project is written in Java 21 and maven

### Supported Commands
- PLACE x y NORTH|EAST|SOUTH|WEST
Places the robot at the given coordinates and facing direction.
Invalid placements (out of bounds or null direction) are ignored.

- MOVE
Moves the robot one unit forward in the direction it is currently facing.
Moves that would cause the robot to fall off the table are ignored.
(also aliased to FORWARD)

- LEFT
Rotates the robot 90 degrees left

- RIGHT
Rotates the robot 90 degrees right

- TURN LEFT|RIGHT
Rotates the robot 90 degrees left or right.

- REPORT
Outputs the robot’s current position and facing direction, or
ROBOT MISSING if the robot has not yet been placed.

The API supports both text/plain and application/json request bodies:
- text/plain is intended for quick manual testing (CLI-style commands).
- application/json is the preferred format for production use.

### API Endpoints (v1):
`/api/v1/command`
Execute a single command.
If a command contains arguments, they are separated by a single space character.

Example:
"PLACE 1 1 NORTH"

Example usages using curl through cmd.exe:
```
curl.exe -X POST http://localhost:8080/api/v1/command -H "Content-Type: text/plain" --data "PLACE 1,1,NORTH"
```
```
curl.exe -X POST http://localhost:8080/api/v1/command -H "Content-Type: text/plain" --data "REPORT"
"PLACE 1,1,NORTH"
```

`/api/v1/commands`
Execute a batch of commands.
Each command is separated by a semicolon.
This is equivalent to sending individual commands to /api/v1/command
The response will be the result of the last command in the batch.

Example:
"PLACE 1,1,NORTH;MOVE;REPORT"

Example usage using curl:
```
curl.exe -X POST http://localhost:8080/api/v1/commands -H "Content-Type: text/plain" --data "PLACE 1,1,NORTH;MOVE;LEFT;REPORT"
1,2,WEST
```
### JSON API:

A JSON API is provided. The Content-Type header should be set to application/json and the request must contain well-formatted JSON.

Response objects are keyed with 'output'

`/api/v1/command`
keyed with "command"
```
curl.exe -X POST http://localhost:8080/api/v1/command -H "Content-Type: application/json" --data "{\"command\": \"REPORT\"}"
{"output":"ROBOT MISSING"}
```

`/api/v1/commands`
keyed with "commands"
```
curl.exe -X POST http://localhost:8080/api/v1/commands -H "Content-Type: application/json" --data "{\"commands\": [\"PLACE 3,3,WEST\",\"REPORT\"]}"
{"output":"3,3,WEST"}
```

Note: When using cmd.exe, the text/plain API may be more convenient due to JSON escaping.
For JSON requests, browser developer tools or API clients (e.g. Postman) may be easier to use.

## Deployment

To start the REST API server locally:

mvn clean test
mvn spring-boot:run

The service will be available at:

http://localhost:8080
