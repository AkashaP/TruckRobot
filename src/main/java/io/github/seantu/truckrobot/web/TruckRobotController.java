package io.github.seantu.truckrobot.web;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1") // v1 API – future versions can coexist under /api/v2
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
