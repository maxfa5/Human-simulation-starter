package org.project.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MetricsService {

    private final MeterRegistry meterRegistry;
    private final Map<String, Counter> authorCounters = new ConcurrentHashMap<>();
    private final AtomicInteger queueSize = new AtomicInteger(0);

    @Autowired
    public MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        initializeMetrics();
    }

    private void initializeMetrics() {
        // Gauge для размера очереди
        Gauge.builder("android.queue.size", queueSize, AtomicInteger::get)
                .description("Current number of tasks in queue")
                .register(meterRegistry);
    }

    public void incrementQueueSize() {
        queueSize.incrementAndGet();
    }

    public void decrementQueueSize() {
        queueSize.decrementAndGet();
    }

    public void setQueueSize(int size) {
        queueSize.set(size);
    }

    public void incrementCompletedTasks(String author) {
        Counter counter = authorCounters.computeIfAbsent(author, 
            key -> Counter.builder("android.completed.tasks")
                    .tag("author", key)
                    .description("Number of completed tasks by author")
                    .register(meterRegistry));
        counter.increment();
    }

    public Map<String, Long> getCompletedTasksByAuthor() {
        Map<String, Long> result = new ConcurrentHashMap<>();
        authorCounters.forEach((author, counter) -> 
            result.put(author, (long) counter.count()));
        return result;
    }

    public int getCurrentQueueSize() {
        return queueSize.get();
    }
} 