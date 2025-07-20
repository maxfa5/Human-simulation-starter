package org.project.service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class WorkloadMonitorService {

    private final Map<String, AtomicInteger> completedTasksByAuthor = new ConcurrentHashMap<>();

    public void taskCompleted(String author) {
        completedTasksByAuthor
            .computeIfAbsent(author, (k) -> new AtomicInteger(0))
            .incrementAndGet();
    }

    public Map<String, Integer> getCompletedTasksByAuthor() {
        return completedTasksByAuthor.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    }
}
