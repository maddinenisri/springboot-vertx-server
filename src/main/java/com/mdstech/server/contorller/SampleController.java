package com.mdstech.server.contorller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mdstech.server.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @GetMapping(path = "/message")
    public String getMessage() {
        return sampleService.latestMessage();
    }

    @PostMapping(path = "/message")
    public String addMessage(@RequestBody JsonNode message) {
        sampleService.addMessage(message.get("message").asText());
        return "{\"status\": \"Success\"}";
    }
}
