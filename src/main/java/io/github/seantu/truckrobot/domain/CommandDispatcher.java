package io.github.seantu.truckrobot.domain;

import org.springframework.stereotype.Component;

@Component
public final class CommandDispatcher {

    private static Integer tryParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return null;
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
     * - TURN LEFT|RIGHT
     *
     * EXAMPLES:
     * - "PLACE 0 0 NORTH"
     * - "TURN RIGHT"
     * - "MOVE"
     *
     * Invalid inputs will generally be ignored.
     * @param command the command expression as specified above
     * @param robot applies parsed commands on this robot object
     */
    public String parseAndApply(String command, TruckRobot robot) {
        if (command == null || robot == null) return null;

        String[] tokens = command.split(" ");

        switch (tokens[0]) {
            case "PLACE" -> {
                if (tokens.length != 4) {
                    return null;
                }
                Integer x = tryParseInt(tokens[1]);
                Integer y = tryParseInt(tokens[2]);
                Facing facing = Facing.parse(tokens[3]);

                if (x == null || y == null || facing == null) {
                    return null;
                }

                robot.place(x, y, facing);
            }
            case "MOVE", "FORWARD" -> {
                robot.move();
            }
            case "TURN" -> {
                if (tokens.length != 2) {
                    return null;
                }
                Direction direction = Direction.parse(tokens[1]);
                if (direction == null) {
                    return null;
                }
                robot.turn(direction);
            }
            case "LEFT" -> {
                if (tokens.length != 1) {
                    return null;
                }
                robot.turn(Direction.LEFT);
            }
            case "RIGHT" -> {
                if (tokens.length != 1) {
                    return null;
                }
                robot.turn(Direction.RIGHT);
            }
            case "REPORT" -> {
                return robot.report();
            }
            default -> { return null; }
        }

        return null;
    }
}
