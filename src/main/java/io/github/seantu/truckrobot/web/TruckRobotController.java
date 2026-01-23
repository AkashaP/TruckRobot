package io.github.seantu.truckrobot.web;

import io.github.seantu.truckrobot.domain.BadCommandException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// v1 API – future versions can be under /api/v2
@RestController
@RequestMapping("/api/v1")
public class TruckRobotController {

    private final TruckRobotService service;

    public TruckRobotController(TruckRobotService service) {
        this.service = service;
    }


    // Plaintext API

    @PostMapping(path = "/command", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String command(@RequestBody String command) {
        String result = this.service.execute(command);
        if (result == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad input");
        return result;
    }

    @PostMapping(path = "/commands", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String commands(@RequestBody String commands) {
        try {
            String result = this.service.executeAll(commands);
            if (result == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad input");
            return result;
        } catch (BadCommandException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad input");
        }
    }

    // JSON API
    // DTOs

    public record CommandRequest(@NotBlank String command) {}
    public record CommandsRequest(@NotEmpty List<String> commands) {}
    public record Response(String output) {}
    public record CommandResult(boolean accepted, boolean report, String output) {}

    @PostMapping(path = "/command", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response commandJson(@Valid @RequestBody CommandRequest command) {
        String result = this.service.execute(command.command());
        if (result == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad input");
        return new Response(result);
    }

    @PostMapping(path = "/commands", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response commandsJson(@Valid @RequestBody CommandsRequest commands) {
        try {
            String result = this.service.executeAll(commands.commands());
            if (result == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad input");
            return new Response(result);
        } catch (BadCommandException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad input");
        }
    }
}
