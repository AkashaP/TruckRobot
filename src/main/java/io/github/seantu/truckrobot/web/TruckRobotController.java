package io.github.seantu.truckrobot.web;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.*;
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
        return this.service.execute(command);
    }

    @PostMapping(path = "/commands", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String commands(@RequestBody String commands) {
        return this.service.executeAll(commands);
    }

    // JSON API
    // DTOs

    public record CommandRequest(@NotBlank String command) {}
    public record CommandsRequest(@NotEmpty List<String> commands) {}
    public record Response(String output) {}

    @PostMapping(path = "/command", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response commandJson(@Valid @RequestBody CommandRequest command) {
        return new Response(this.service.execute(command.command()));
    }

    @PostMapping(path = "/commands", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response commandsJson(@Valid @RequestBody CommandsRequest commands) {
        return new Response(this.service.executeAll(commands.commands()));
    }
}
