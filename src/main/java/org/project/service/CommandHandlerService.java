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
import java.util.concurrent.BlockingQueue;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.project.exception.QueueOverflowException;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CommandHandlerService {
    private final int CORE_POOL_SIZE = 2;
    private final int MAXIMUM_POOL_SIZE = 5;
    private final long KEEP_ALIVE_TIME = 60L;
    private final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private final int QUEUE_SIZE = 100;
    
    private final ThreadPoolExecutor commandExecutor;
    private static final Logger logger = LoggerFactory.getLogger(CommandHandlerService.class);
    private  WorkloadMonitorService workloadMonitorService;
    private final MetricsService metricsService;

    @Autowired
    public CommandHandlerService(WorkloadMonitorService workloadMonitorService, MetricsService metricsService) {
        this.workloadMonitorService = workloadMonitorService;
        this.metricsService = metricsService;
        

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(QUEUE_SIZE);
        this.commandExecutor = new ThreadPoolExecutor(
            CORE_POOL_SIZE, 
            MAXIMUM_POOL_SIZE,
            KEEP_ALIVE_TIME, 
            KEEP_ALIVE_TIME_UNIT, 
            workQueue,
            new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    throw new QueueOverflowException("Command queue is full");
                }
            }
        );
    }

    public void handleCommand(Command command) {
        if (command.getPriority() == Priority.CRITICAL) {
            executeCommand(command);
        } else {
            try {
                commandExecutor.execute(() -> executeCommand(command));
                metricsService.incrementQueueSize();
            } catch (QueueOverflowException e) {
                throw e;
            }
        }
    }

    public int getQueueSize() {
        return commandExecutor.getQueue().size();
    }
    
    private void executeCommand(Command command) {
        logger.info("Executing command: {}", command);
        workloadMonitorService.taskCompleted(command.getAuthor());
        metricsService.incrementCompletedTasks(command.getAuthor());
        metricsService.decrementQueueSize();
    }
}
