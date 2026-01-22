package io.github.seantu.truckrobot;

public enum Direction {
    LEFT,
    RIGHT;

    public static Direction parse(String direction) {
        switch (direction) {
            case "LEFT" -> {
                return Direction.LEFT;
            }
            case "RIGHT" -> {
                return Direction.RIGHT;
            }
        }
        return null;
    }
}
