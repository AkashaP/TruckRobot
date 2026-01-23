package io.github.seantu.truckrobot.domain;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CommandBatchParser {
    /**
     * Takes a batch of commands into a format that can be dispatched individually
     * @param commands the commands, separated by commas
     * @return a list of individual commands
     */
    public List<String> parseCommands(String commands) {
        return Arrays.asList(commands.trim().split(","));
    }
}
