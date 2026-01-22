package io.github.seantu.truckrobot.domain;

import org.springframework.stereotype.Component;

/**
 * Models a TruckRobot operating in a grid of set size.
 *
 * The robot begins in an unplaced state.
 * While unplaced, movement and rotation commands are ignored.
 * All commands are validated to ensure the robot never leaves the table.
 */
@Component
public final class TruckRobot {

    private static final int MIN_X = 0;
    private static final int MAX_X = 4;
    private static final int MIN_Y = 0;
    private static final int MAX_Y = 4;

    private int x;
    private int y;
    private Facing facing;
    private boolean placed;

    /**
     * Checks if the location is in bounds
     * @param x x location
     * @param y y loation
     * @return true if in bounds, false otherwise
     */
    private boolean isInBounds(int x, int y) {
        return x >= MIN_X && x <= MAX_X && y >= MIN_Y && y <= MAX_Y;
    }

    /**
     * Moves the truck forward in its facing direction if possible
     **/
    public void forward() {
        if (!placed) {
            return;
        }

        int dx = 0;
        int dy = 0;
        switch (facing) {
            case Facing.NORTH ->
                dy = 1;
            case Facing.EAST ->
                dx = 1;
            case Facing.SOUTH ->
                dy = -1;
            case Facing.WEST ->
                dx = -1;
        }

        int newX = x + dx;
        int newY = y + dy;

        if (isInBounds(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    /**
     * Places the TruckRobot at the specified location and facing direction.
     * This is ignored if it is out of bounds or null facing direction
     * @param x int between MIN_X and MAX_X
     * @param y int between MIN_Y and MAX_Y
     * @param facing facing direction
     */
    public void place(int x, int y, Facing facing) {
        if (!isInBounds(x, y) || facing == null) {
            return;
        }
        this.x = x;
        this.y = y;
        this.facing = facing;
        this.placed = true;
    }

    /**
     * Turns the TruckRobot in the specified direction.
     * This is ignored if the TruckRobot hasn't been placed yet or null facing direction
     * @param direction the direction to turn.
     */
    public void turn(Direction direction) {
        if (!placed)
            return;
        switch (direction) {
            case Direction.LEFT -> {
                switch (facing) {
                    case NORTH -> facing = Facing.WEST;
                    case EAST -> facing = Facing.NORTH;
                    case SOUTH -> facing = Facing.EAST;
                    case WEST -> facing = Facing.SOUTH;
                }
            }
            case Direction.RIGHT -> {
                switch (facing) {
                    case NORTH -> facing = Facing.EAST;
                    case EAST -> facing = Facing.SOUTH;
                    case SOUTH -> facing = Facing.WEST;
                    case WEST -> facing = Facing.NORTH;
                }
            }
        }
    }

    /**
     * Generates a report of the TruckRobot's location in the format:
     * X,Y,FACING
     *
     * This will print ROBOT MISSING if it is not yet placed in the field.
     * @return the report
     */
    public String report() {
        if (isInBounds(x, y) && isPlaced()) {
            return x + "," + y + "," + facing;
        }
        else {
            return "ROBOT MISSING";
        }
    }

    // Getters

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Facing getFacing() {
        return facing;
    }

    public boolean isPlaced() {
        return placed;
    }
}
