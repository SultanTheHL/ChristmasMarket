package de.tum.cit.aet.pse.controller;

import de.tum.cit.aet.pse.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/santa")
public class SantaController {
    @Autowired
    private EventService eventService;

    @PostMapping("/trigger-event")
    public String triggerSantaEvent(@RequestParam String eventName, @RequestParam double amount) {
        return eventService.triggerSantaEvent(eventName, amount);
    }
}