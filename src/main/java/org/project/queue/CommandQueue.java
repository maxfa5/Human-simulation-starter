package org.project.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.project.model.Command;
import org.springframework.stereotype.Component;

@Component
public class CommandQueue {
    private final int MAX_QUEUE_SIZE = 100;
    private final BlockingQueue<Command> queue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);

    public boolean addCommand(Command command) {
        return queue.offer(command);
    }

    public Command takeCommand() throws InterruptedException {
        return queue.take();
    }
}