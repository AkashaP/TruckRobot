package io.github.seantu.truckrobot;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TruckRobotService {
    private final TruckRobot domain;
    private final CommandDispatcher dispatcher;
    private final CommandBatchParser batchParser;

    public TruckRobotService(TruckRobot domain, CommandDispatcher dispatcher, CommandBatchParser batchParser) {
        this.domain = domain;
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
     * @param commands list of commands to execute, separated by commas.
     * @return result of the last command executed
     */
    public synchronized String executeAll(String commands) {
        List<String> commandsTokens = batchParser.parseCommands(commands);
        String result = null;
        for (String command : commandsTokens) {
            result = execute(command);
        }
        return result;
    }
}
