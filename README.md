Truck Robot REST API

This is an application that controls a virtual truck robot moving in a table.
A REST API is provided where commands can be submitted over HTTP to control the truck robot.

Supported Commands:
PLACE x(int) y(int) NORTH|EAST|SOUTH|WEST
FORWARD
REPORT
TURN LEFT|RIGHT

API Endpoints (v1):
/api/v1/command
Execute a single command.
If a command contains arguments, they are separated by a single space character.

Example:
"PLACE 1 1 NORTH"

Example usage using curl:
curl.exe -X POST http://localhost:8080/api/v1/command -H "Content-Type: text/plain" --data "PLACE 1 1 NORTH"
curl.exe -X POST http://localhost:8080/api/v1/command -H "Content-Type: text/plain" --data "REPORT"

/api/v1/commands
Execute a batch of commands.
Each command is separated by a comma.
This is equivalent to sending individual commands to /api/v1/command

Example:
"PLACE 1 1 NORTH,FORWARD,REPORT"

Example usage using curl:
curl.exe -X POST http://localhost:8080/api/v1/commands -H "Content-Type: text/plain" --data "PLACE 1 1 NORTH,FORWARD,TURN LEFT,REPORT"