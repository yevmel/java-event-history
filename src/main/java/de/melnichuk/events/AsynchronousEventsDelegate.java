package de.melnichuk.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class AsynchronousEventsDelegate<T> implements Events<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsynchronousEventsDelegate.class);

    private final ThreadPoolExecutor executorService;
    private final Events<T> events;
    private final int backlogSize;

    public AsynchronousEventsDelegate(final Events<T> events, final int backlogSize) {
        this.backlogSize = backlogSize;
        this.events = events;

        // single threaded executor to ensure succession
        this.executorService = new ThreadPoolExecutor(
            1, 1,
            5, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());
    }

    @Override
    public void process(T item) {
        if (executorService.getQueue().size() < backlogSize) {
            executorService.submit(() -> {
                events.process(item);
            });
        } else {
            LOGGER.warn("dropping item, because there are already to many items in backlog.");
        }
    }

    @Override
    public Map<Integer, ? extends List<T>> getHistory() {
        return events.getHistory();
    }
}
