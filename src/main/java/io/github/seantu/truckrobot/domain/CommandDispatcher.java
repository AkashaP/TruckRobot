package io.github.seantu.truckrobot.domain;

import org.springframework.stereotype.Component;

@Component
public final class CommandDispatcher {

    private static Integer tryParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            throw new BadCommandException("BAD_COMMAND", "Invalid integer: " + s);
        }
    }

    /**
     * Takes a command as a string and applies it to the robot object.
     * The command is an expression consisting of:
     * - the command in CAPITAL CASE, followed by:
     * - one or more arguments with exactly one space between.
     *
     * Supported commands:
     * - PLACE x(int) y(int) NORTH|EAST|SOUTH|WEST
     * - MOVE
     * - REPORT
     * - LEFT
     * - RIGHT
     *
     * EXAMPLES:
     * - "PLACE 0,0,NORTH"
     * - "TURN RIGHT"
     * - "MOVE"
     *
     * Invalid commands return null (caller should treat as bad input).
     * @param command the command expression as specified above
     * @param robot applies parsed commands on this robot object
     */
    public String parseAndApply(String command, TruckRobot robot) {
        if (command == null || robot == null) return null;

        String[] tokens = command.trim().split("\\s+");

        if (tokens.length == 0) {
            return null;
        }

        switch (tokens[0]) {
            case "PLACE" -> {
                if (tokens.length != 2) throw new BadCommandException("BAD_COMMAND", "PLACE requires X,Y,F");
                String[] placeArgs = tokens[1].split(",");
                if (placeArgs.length != 3) throw new BadCommandException("BAD_COMMAND", "PLACE requires X,Y,F");

                int x = tryParseInt(placeArgs[0]);
                int y = tryParseInt(placeArgs[1]);
                Facing facing = Facing.parse(placeArgs[2]);
                if (facing == null) throw new BadCommandException("BAD_COMMAND", "Invalid facing: " + placeArgs[2]);

                robot.place(x, y, facing);
                return "";
            }
            case "MOVE", "FORWARD" -> {
                if (tokens.length != 1) throw new BadCommandException("BAD_COMMAND", "MOVE takes no args");
                robot.move();
                return "";
            }
            case "LEFT" -> {
                if (tokens.length != 1) throw new BadCommandException("BAD_COMMAND", "LEFT takes no args");
                robot.turn(Direction.LEFT);
                return "";
            }
            case "RIGHT" -> {
                if (tokens.length != 1) throw new BadCommandException("BAD_COMMAND", "RIGHT takes no args");
                robot.turn(Direction.RIGHT);
                return "";
            }
            case "REPORT" -> {
                if (tokens.length != 1) throw new BadCommandException("BAD_COMMAND", "REPORT takes no args");
                return robot.report();
            }
            default -> throw new BadCommandException("BAD_COMMAND", "Unknown command: " + tokens[0]);
        }
    }
}
