package io.github.seantu.truckrobot;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TruckRobotController {

    private final TruckRobotService service;

    public TruckRobotController(TruckRobotService service) {
        this.service = service;
    }

    @PostMapping("/command")
    public String command(@RequestBody String command) {
        return this.service.execute(command);
    }

    @PostMapping("/commands")
    public String commands(@RequestBody String commands) {
        return this.service.executeAll(commands);
    }
}
