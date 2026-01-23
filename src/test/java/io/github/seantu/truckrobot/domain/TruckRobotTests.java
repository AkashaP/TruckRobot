package io.github.seantu.truckrobot.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TruckRobotTests {

    // Initial TDD tests

    @Test
    void truckIsNotPlacedUntilPlacedIsCalled() {
        TruckRobot robot = new TruckRobot();
        assertFalse(robot.isPlaced());
        robot.place(0,0, Facing.SOUTH);
        assertTrue(robot.isPlaced());
        assertEquals("0,0,SOUTH", robot.report());
    }

    @Test
    void truckReportIsConsistent() {
        TruckRobot robot = new TruckRobot();
        robot.place(0,0, Facing.SOUTH);
        assertEquals("0,0,SOUTH", robot.report());
        robot.place(1,1, Facing.WEST);
        assertEquals("1,1,WEST", robot.report());
        robot.place(4,4, Facing.EAST);
        assertEquals("4,4,EAST", robot.report());
        robot.place(3,3, Facing.NORTH);
        assertEquals("3,3,NORTH", robot.report());
    }

    @Test
    void truckIgnoresTurnsWhenNotPlaced() {
        TruckRobot robot = new TruckRobot();
        robot.turn(Direction.LEFT);
        robot.turn(Direction.RIGHT);
        assertEquals("ROBOT MISSING", robot.report());
    }

    @Test
    void truckMovesMove() {
        TruckRobot robot = new TruckRobot();
        robot.place(0,0, Facing.NORTH);
        robot.move();
        assertEquals("0,1,NORTH", robot.report());
        robot.move();
        assertEquals("0,2,NORTH", robot.report());
    }

    @Test
    void truckTurns() {
        TruckRobot robot = new TruckRobot();
        robot.place(0,0, Facing.NORTH);
        robot.turn(Direction.RIGHT);
        assertEquals("0,0,EAST", robot.report());
        robot.turn(Direction.RIGHT);
        assertEquals("0,0,SOUTH", robot.report());
        robot.turn(Direction.RIGHT);
        assertEquals("0,0,WEST", robot.report());
        robot.turn(Direction.LEFT);
        assertEquals("0,0,SOUTH", robot.report());
        robot.turn(Direction.LEFT);
        assertEquals("0,0,EAST", robot.report());
        robot.turn(Direction.LEFT);
        assertEquals("0,0,NORTH", robot.report());
    }

    @Test
    void truckMovesInSnakePath() {
        TruckRobot robot = new TruckRobot();
        robot.place(0,2, Facing.NORTH);

        // Move right by one
        robot.turn(Direction.RIGHT);
        assertEquals("0,2,EAST", robot.report());
        robot.move();
        assertEquals("1,2,EAST", robot.report());

        // Go to the bottom
        robot.turn(Direction.RIGHT);
        assertEquals("1,2,SOUTH", robot.report());
        robot.move();
        assertEquals("1,1,SOUTH", robot.report());

        // Move right by one
        robot.turn(Direction.LEFT);
        assertEquals("1,1,EAST", robot.report());
        robot.move();
        assertEquals("2,1,EAST", robot.report());

        // Go back up
        robot.turn(Direction.LEFT);
        assertEquals("2,1,NORTH", robot.report());
        robot.move();
        assertEquals("2,2,NORTH", robot.report());
        robot.move();
        assertEquals("2,3,NORTH", robot.report());
    }

    @Test
    void truckIgnoresOutOfBoundsPlacement() {
        TruckRobot robot = new TruckRobot();
        robot.place(0,0, Facing.SOUTH);
        robot.place(-1,-1, Facing.SOUTH);
        assertEquals("0,0,SOUTH", robot.report());
        robot.place(5,0, Facing.NORTH);
        assertEquals("0,0,SOUTH", robot.report());
        robot.place(5,5, Facing.EAST);
        assertEquals("0,0,SOUTH", robot.report());
    }

    @Test
    void truckIgnoresMoveWhenNotPlaced() {
        TruckRobot robot = new TruckRobot();
        robot.move();
        assertEquals("ROBOT MISSING", robot.report());
    }

    @Test
    void truckDoesntMoveMoveIfAtEdge() {
        TruckRobot robot = new TruckRobot();
        robot.place(0,0, Facing.SOUTH);
        robot.move();
        assertEquals("0,0,SOUTH", robot.report());

        robot.place(0,4, Facing.NORTH);
        robot.move();
        assertEquals("0,4,NORTH", robot.report());

        robot.place(4,4, Facing.EAST);
        robot.move();
        assertEquals("4,4,EAST", robot.report());

        robot.place(0,0, Facing.WEST);
        robot.move();
        assertEquals("0,0,WEST", robot.report());
    }
}
