package io.github.seantu.truckrobot.domain;

public enum Facing {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public static Facing parse(String facing) {
        switch (facing) {
            case "NORTH" -> {
                return Facing.NORTH;
            }
            case "EAST" -> {
                return Facing.EAST;
            }
            case "SOUTH" -> {
                return Facing.SOUTH;
            }
            case "WEST" -> {
                return Facing.WEST;
            }
        }
        return null;
    }
}
