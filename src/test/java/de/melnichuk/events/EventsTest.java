package de.melnichuk.events;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.IntStream;

public class EventsTest {

    @Test
    public void shouldConsiderAllIncrements() {
        // when
        final Events<Integer> events = new Events<>(10, 1, 5, 10);

        // then
        Assert.assertThat(events.getHistory().keySet(), Matchers.contains(1, 5, 10));
    }

    @Test
    public void shouldAlwaysAddFirstItem() {
        // given
        final Events<Integer> events = new Events<>(10, 2, 5, 10);

        // when
        events.process(23);

        // then
        events.getHistory().entrySet().stream()
            .map(e -> e.getValue())
            .forEach(l -> {
                Assert.assertEquals(1, l.size());
                Assert.assertEquals(23, l.get(0).intValue());
            });
    }

    @Test
    public void TODO_define_asserts() {
        // given
        final Events<Integer> events = new Events<>(20, 10, 1_000, 10_000);

        // when
        IntStream.range(0, 100_000).forEach(events::process);

        // then
        events.getHistory().values().stream().forEach(System.out::println);
    }
}
