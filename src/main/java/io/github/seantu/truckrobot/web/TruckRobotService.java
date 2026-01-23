package io.github.seantu.truckrobot.web;

import io.github.seantu.truckrobot.domain.BadCommandException;
import io.github.seantu.truckrobot.domain.CommandBatchParser;
import io.github.seantu.truckrobot.domain.CommandDispatcher;
import io.github.seantu.truckrobot.domain.TruckRobot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruckRobotService {
    private final TruckRobot domain;
    private final CommandDispatcher dispatcher;
    private final CommandBatchParser batchParser;

    public TruckRobotService(CommandDispatcher dispatcher, CommandBatchParser batchParser) {
        this.domain = new TruckRobot();
        this.dispatcher = dispatcher;
        this.batchParser = batchParser;
    }

    /**
     * Execute a command
     * @see CommandDispatcher:parseAndApply
     * @param command the command to execute
     */
    public synchronized String execute(String command) {
        return dispatcher.parseAndApply(command, domain);
    }

    /**
     * Execute commands in batch
     * @param commands list of commands to execute
     * @throws BadCommandException if one of the commands is not formatted correctly.
     * @return result of the last command executed
     */
    public synchronized String executeAll(List<String> commands) {
        String result = null;
        for (String command : commands) {
            result = execute(command);
            if (result == null) {
                throw new BadCommandException("BAD_COMMAND", "A command in a batch was not valid");
            }
        }
        return result;
    }

    /**
     * Execute commands in batch
     * @param commands string of commands to execute, each command is separated by a comma.
     * @return result of the last command executed
     */
    public synchronized String executeAll(String commands) {
        List<String> commandsTokens = batchParser.parseCommands(commands);
        return executeAll(commandsTokens);
    }
}
