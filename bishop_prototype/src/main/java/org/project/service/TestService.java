package org.project.service;

import org.project.anotation.WeylandWatchingYou;
import org.project.model.AuditMode;
import org.project.model.Priority;
import org.springframework.stereotype.Service;
import org.project.model.Command;
import org.project.service.CommandHandlerService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TestService {
    private CommandHandlerService commandHandlerService;
    @Autowired
    public TestService(CommandHandlerService commandHandlerService) {
        this.commandHandlerService = commandHandlerService;
    }
    @WeylandWatchingYou(auditMode = AuditMode.KAFKA, auditTopic = "test")
    public String test(int a) {
        // Fix: Use the correct Command constructor
        Command command = new Command();
        command.setPriority(Priority.COMMON);
        command.setDescription("testDescription");
        command.setAuthor("testAuthor");
        commandHandlerService.handleCommand(command);
        System.out.println("test");
        return "test";
    }
}