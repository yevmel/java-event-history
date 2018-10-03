package de.melnichuk.events;

import java.util.LinkedList;

/**
 * a sampling strategy is used to pick a sample from a list of items or calculate one.
 * <p>
 * you may want to us a custom sampling strategy to prevent a spike in your data from tampering with your observations.
 *
 * @param <T>
 */
public interface SamplingStrategy<T> {
    T getSample(LinkedList<T> items);
}
