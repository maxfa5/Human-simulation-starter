package org.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.project.service.TestService;
import org.project.service.CommandHandlerService;
import org.project.model.Command;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommandController {
    @Autowired
    private CommandHandlerService commandHandlerService;
    
    @PostMapping("/api/commands")
    public ResponseEntity<String> handleCommand(@RequestBody Command command) {
        commandHandlerService.handleCommand(command);
        return ResponseEntity.ok("Command processed");
    }
}