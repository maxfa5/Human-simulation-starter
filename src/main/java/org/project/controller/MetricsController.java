package org.project.controller;

import org.project.service.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final MetricsService metricsService;

    @Autowired
    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/queue-size")
    public int getQueueSize() {
        return metricsService.getCurrentQueueSize();
    }

    @GetMapping("/completed-tasks")
    public Map<String, Long> getCompletedTasksByAuthor() {
        return metricsService.getCompletedTasksByAuthor();
    }

    @GetMapping("/summary")
    public Map<String, Object> getMetricsSummary() {
        return Map.of(
            "queueSize", metricsService.getCurrentQueueSize(),
            "completedTasksByAuthor", metricsService.getCompletedTasksByAuthor()
        );
    }
} 