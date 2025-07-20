package org.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.project.model.Command;
import org.project.model.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.project.exception.QueueOverflowException;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CommandHandlerService {

    private final BlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>(100);
    private static final Logger logger = LoggerFactory.getLogger(CommandHandlerService.class);
    private  WorkloadMonitorService workloadMonitorService;
    private final MetricsService metricsService;

    @Autowired
    public CommandHandlerService(WorkloadMonitorService workloadMonitorService, MetricsService metricsService) {
        this.workloadMonitorService = workloadMonitorService;
        this.metricsService = metricsService;
    }

    public Command takeCommand() throws InterruptedException {
        Command command = commandQueue.take();
        metricsService.decrementQueueSize();
        return command;
    }

    public void handleCommand(Command command) {
        if (command.getPriority() == Priority.CRITICAL) {
            executeCommand(command);
        } else {
            boolean added = commandQueue.add(command);
            if (!added) {
                throw new QueueOverflowException("Command queue is full");
            }
            metricsService.incrementQueueSize();
        }
    }

    public int getQueueSize() {
        return commandQueue.size();
    }
    
    private void executeCommand(Command command) {
        logger.info("Executing command: {}", command);
        commandQueue.remove(command);
        workloadMonitorService.taskCompleted(command.getAuthor());
        metricsService.incrementCompletedTasks(command.getAuthor());
    }
}
