package de.melnichuk.events;

import java.util.List;
import java.util.Map;

/**
 * collects supplied items, usually holding information about occurred events.
 * <p>
 * amount of collected items/information depends on the implementation.
 */
public interface Events<T> {
    void process(T item);

    Map<Integer, ? extends List<T>> getHistory();
}
