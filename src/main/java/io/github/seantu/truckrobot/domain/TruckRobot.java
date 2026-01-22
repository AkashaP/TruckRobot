package io.github.seantu.truckrobot.domain;

import org.springframework.stereotype.Component;

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

    public void place(int x, int y, Facing facing) {
        if (!isInBounds(x, y) || facing == null) {
            return;
        }
        this.x = x;
        this.y = y;
        this.facing = facing;
        this.placed = true;
    }

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
