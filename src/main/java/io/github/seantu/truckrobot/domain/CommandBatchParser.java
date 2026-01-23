package io.github.seantu.truckrobot.domain;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CommandBatchParser {
    /**
     * Takes a batch of commands into a format that can be dispatched individually
     * @param commands the commands, separated by semicolons
     * @return a list of individual commands
     */
    public List<String> parseCommands(String commands) {
        if (commands == null || commands.isBlank()) {
            throw new BadCommandException("BAD_COMMAND", "Commands are empty");
        }
        List<String> result = Arrays.asList(commands.trim().split(";"));
        if (result.isEmpty()) {
            throw new BadCommandException("BAD_COMMAND", "No valid commands found");
        }
        return result;
    }
}
