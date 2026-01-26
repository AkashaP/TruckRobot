package io.github.seantu.truckrobot.web;

import io.github.seantu.truckrobot.domain.BadCommandException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// v1 API – future versions can be under /api/v2
@RestController
@Validated
@RequestMapping("/api/v1")
public class TruckRobotController {

    private final TruckRobotService service;

    public TruckRobotController(TruckRobotService service) {
        this.service = service;
    }

    // Plaintext API

    @PostMapping(path = "/command", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> command(@RequestBody String command) {
        try {
            String result = this.service.execute(command);

            // Report HTTP 400
            if (result == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad input");

            // Report HTTP 204
            if (result.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            // Report HTTP 200
            return ResponseEntity.ok(result);
        } catch (BadCommandException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad input");
        }
    }

    @PostMapping(path = "/commands", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> commands(@RequestBody String commands) {
        try {
            String result = this.service.executeAll(commands);

            // Report HTTP 400
            if (result == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad input");

            // Report HTTP 204
            if (result.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            // Report HTTP 200
            return ResponseEntity.ok(result);
        } catch (BadCommandException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad input");
        }
    }

    // JSON API
    // DTOs

    public record CommandRequest(@NotNull @NotBlank String command) {
    }

    public record CommandsRequest(@NotNull @NotEmpty List<String> commands) {
    }

    public record Response(String output) {
    }

    @PostMapping(path = "/command", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> commandJson(@Valid @RequestBody CommandRequest command) {
        try {
            String result = this.service.execute(command.command());

            // Report HTTP 400
            if (result == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad input");
            }

            // Report HTTP 200
            return ResponseEntity.ok(new Response(result));
        } catch (BadCommandException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad input");
        }
    }

    @PostMapping(path = "/commands", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> commandsJson(@Valid @RequestBody CommandsRequest commands) {
        try {
            String result = this.service.executeAll(commands.commands());

            // Report HTTP 400
            if (result == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad input");

            // Report HTTP 200
            return ResponseEntity.ok(new Response(result));
        } catch (BadCommandException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad input");
        }
    }
}
