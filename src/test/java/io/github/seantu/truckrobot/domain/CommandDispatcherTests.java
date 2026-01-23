package io.github.seantu.truckrobot.domain;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class CommandDispatcherTests {

    // Test suite to verify if the command parser is executing the correct command.

    @Test
    void testCommandForward() {
        TruckRobot robot = mock(TruckRobot.class);
        // robot.forward();
        var parser = new CommandDispatcher();
        parser.parseAndApply("MOVE", robot);
        verify(robot).move();
    }

    @Test
    void testCommandReport() {
        TruckRobot robot = mock(TruckRobot.class);

        CommandDispatcher parser = new CommandDispatcher();
        parser.parseAndApply("REPORT", robot);
        verify(robot).report();
    }

    @Test
    void testCommandTurn() {
        TruckRobot robot = mock(TruckRobot.class);

        CommandDispatcher parser = new CommandDispatcher();
        parser.parseAndApply("TURN LEFT", robot);
        verify(robot).turn(Direction.LEFT);
        reset(robot);
        parser.parseAndApply("TURN RIGHT", robot);
        verify(robot).turn(Direction.RIGHT);
        reset(robot);
        parser.parseAndApply("LEFT", robot);
        verify(robot).turn(Direction.LEFT);
        reset(robot);
        parser.parseAndApply("RIGHT", robot);
        verify(robot).turn(Direction.RIGHT);
    }

    @Test
    void testCommandPlace() {
        TruckRobot robot = mock(TruckRobot.class);

        CommandDispatcher parser = new CommandDispatcher();
        parser.parseAndApply("PLACE 2 2 NORTH", robot);
        verify(robot).place(2, 2, Facing.NORTH);
    }
}
