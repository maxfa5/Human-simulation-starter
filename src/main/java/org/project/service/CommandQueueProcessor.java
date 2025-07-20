package org.project.service;

import org.project.model.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.project.service.CommandHandlerService;
import org.project.service.WorkloadMonitorService;
import jakarta.annotation.PostConstruct;

@Service
public class CommandQueueProcessor {
    private final MetricsService metricsService;
    private final CommandHandlerService commandHandlerService;
    private final WorkloadMonitorService workloadMonitorService;

    private volatile boolean running = true;
    
    @Autowired
    public CommandQueueProcessor(MetricsService metricsService, CommandHandlerService commandHandlerService, 
                                WorkloadMonitorService workloadMonitorService) {
        this.metricsService = metricsService;
        this.commandHandlerService = commandHandlerService;
        this.workloadMonitorService = workloadMonitorService;
    }
    
    @PostConstruct
    public void startProcessing() {
        Thread processorThread = new Thread(this::processQueue);
        processorThread.setDaemon(true);
        processorThread.start();
    }
    
    private void processQueue() {
        while (running) {
            try {
                Command command = commandHandlerService.takeCommand();
                executeCommand(command);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    private void executeCommand(Command command) {
        System.out.println("Processing command: " + command);
        workloadMonitorService.taskCompleted(command.getAuthor());
        metricsService.incrementCompletedTasks(command.getAuthor());
    }
    
    public void stop() {
        running = false;
    }
}
