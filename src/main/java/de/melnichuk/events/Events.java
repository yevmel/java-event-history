package de.melnichuk.events;

import java.util.*;
import java.util.stream.Collectors;

public class Events<T> {

    /**
     * holds samples of different resolution.
     * <p>
     * key is the increment (aka. resolution)
     * value is the list of length {@code threshold} holding samples.
     */
    private final Map<Integer, LinkedList<T>> history = new HashMap<>();

    /**
     * default behaviour ist to pick the first item assuming first==newest
     */
    private SamplingStrategy<T> samplingStrategy = LinkedList::getFirst;

    /**
     * max number of items in a list holding samples.
     */
    private final int threshold;

    /**
     * items stored for sampling strategy to work with
     */
    private LinkedList<T> itemBacklog = new LinkedList<>();

    /**
     * number of items to remember for sampling strategy to work with
     */
    private int itemBacklogCount = 10;

    /**
     * number of items processed.
     */
    private long count;

    public Events(int threshold, int... increments) {
        this.threshold = threshold;
        Arrays.stream(increments).forEach(i -> history.put(i, new LinkedList<>()));
    }

    public void process(T item) {
        try {
            doProcess(item);
        } finally {
            count++;
        }
    }

    private void doProcess(T item) {
        addItemToList(item, itemBacklog, itemBacklogCount);

        // select list of samples with matching increment
        final List<Map.Entry<Integer, LinkedList<T>>> matchingIncrementsAndSamples = history.entrySet().stream()
            .filter(incrementAndSamples -> count % incrementAndSamples.getKey() == 0)
            .collect(Collectors.toList());

        if (!matchingIncrementsAndSamples.isEmpty()) {
            final T sample = samplingStrategy.getSample(itemBacklog);
            matchingIncrementsAndSamples.stream()
                .map(incrementAndSamples -> incrementAndSamples.getValue())
                .forEach(samples -> {
                    addItemToList(sample, samples, threshold);
                });
        }
    }

    private void addItemToList(final T event, final LinkedList<T> samples, final int threshold) {
        samples.addFirst(event);

        if (samples.size() > threshold) {
            samples.removeLast();
        }
    }

    public Map<Integer, ? extends List<T>> getHistory() {
        return history;
    }
}
