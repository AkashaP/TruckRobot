package io.github.seantu.truckrobot;

public final class TruckRobot {

    private int x;
    private int y;
    private Facing facing;
    private boolean placed;

    /**
     * Moves the truck forward in its facing direction if possible
     **/
    public void forward() {

    }

    public void place(int x, int y, Facing facing) {

    }

    public void turn(Direction direction) {

    }

    public String report() {
        return "";
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
