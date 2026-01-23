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
     * - LEFT
     * - RIGHT
     *
     * EXAMPLES:
     * - "PLACE 0 0 NORTH"
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
                if (tokens.length != 2) {
                    return null;
                }
                String[] placeArgs = tokens[1].split(",");

                if (placeArgs.length != 3) {
                    return null;
                }

                Integer x = tryParseInt(placeArgs[0]);
                Integer y = tryParseInt(placeArgs[1]);
                Facing facing = Facing.parse(placeArgs[2]);

                if (x == null || y == null || facing == null) {
                    return null;
                }

                robot.place(x, y, facing);
                return "";
            }
            case "MOVE", "FORWARD" -> {
                robot.move();
                return "";
            }
            case "LEFT" -> {
                if (tokens.length != 1) {
                    return null;
                }
                robot.turn(Direction.LEFT);
                return "";
            }
            case "RIGHT" -> {
                if (tokens.length != 1) {
                    return null;
                }
                robot.turn(Direction.RIGHT);
                return "";
            }
            case "REPORT" -> {
                return robot.report();
            }
            default -> { return null; }
        }

    }
}
