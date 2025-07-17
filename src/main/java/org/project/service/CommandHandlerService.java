package org.project.service;

import org.project.model.Command;
import org.project.model.Priority;
import org.project.queue.CommandQueue;

public class CommandHandlerService {
    private final CommandQueue commandQueue = new CommandQueue();

    public void handleCommand(Command command) {
        if (command.getPriority() == Priority.CRITICAL) {
            executeCommand(command);
        } else {
            boolean added = commandQueue.addCommand(command);
            if (!added) {
                throw new RuntimeException("Command queue is full");
            }
        }
    }

    private void executeCommand(Command command) {
        // Логика исполнения команды
    }
}
