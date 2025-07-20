package org.project.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.project.service.WorkloadMonitorService;
import org.project.service.CommandHandlerService;
import java.util.Map;


public class MetricsPrinterService {

    private  CommandHandlerService commandHandlerService;
    private  WorkloadMonitorService workloadMonitorService;
    @Autowired
    public MetricsPrinterService(CommandHandlerService commandHandlerService, WorkloadMonitorService workloadMonitorService) {
        this.commandHandlerService = commandHandlerService;
        this.workloadMonitorService = workloadMonitorService;
    }

    public void printQueueSize() {
        int queueSize = commandHandlerService.getQueueSize();
        System.out.println("=== ТЕКУЩАЯ ЗАНЯТОСТЬ АНДРОИДА ===");
        System.out.println("Количество задач в очереди: " + queueSize);
        System.out.println("================================");
    }

    public void printCompletedTasksByAuthor() {
        Map<String, Integer> completedTasksByAuthor = workloadMonitorService.getCompletedTasksByAuthor();
        System.out.println("=== ЭФФЕКТИВНОСТЬ РАБОТЫ ПО АВТОРАМ ===");
        if (completedTasksByAuthor.isEmpty()) {
            System.out.println("Пока нет выполненных заданий.");
        } else {
            completedTasksByAuthor.forEach((author, count) -> {
                System.out.println("Автор: " + author + " | Выполнено заданий: " + count);
            });
        }
        System.out.println("=======================================");
    }

    public void printAllMetrics() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           МОНИТОРИНГ АНДРОИДА");
        System.out.println("=".repeat(50));
        
        printQueueSize();
        System.out.println();
        printCompletedTasksByAuthor();
        
        System.out.println("=".repeat(50) + "\n");
    }
}