package de.melnichuk.events;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class EventsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsTest.class);

    @Test
    public void shouldConsiderAllIncrements() {
        // when
        final DefaultEvents<Integer> events = new DefaultEvents<>(10, 1, 5, 10);

        // then
        Assert.assertThat(events.getHistory().keySet(), Matchers.contains(1, 5, 10));
    }

    @Test
    public void shouldAlwaysAddFirstItem() {
        // given
        final DefaultEvents<Integer> events = new DefaultEvents<>(10, 2, 5, 10);

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
    public void TODO_define_asserts() throws Exception {
        // given
        final Events<Integer> events = new AsynchronousEventsDelegate<>(
            new DefaultEvents<>(20, 10, 1_000, 10_000),
            20
        );

        // when
        IntStream.range(0, 100_000).forEach(events::process);

        // then
        TimeUnit.SECONDS.sleep(2);
        events.getHistory().values().stream().forEach(System.out::println);
    }
}
