package org.febit.demo.temporal.workflow;

import io.temporal.testing.TestActivityEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.febit.demo.temporal.BaseTemporalTest;
import org.febit.demo.temporal.workflow.ScheduleDefs.ConsumerActivity;
import org.febit.demo.temporal.workflow.ScheduleDefs.ProducerActivity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.List;

import static org.febit.demo.temporal.workflow.impl.ConsumerActivityImpl.POLL_TIMEOUT_MS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Slf4j
class ScheduleActivitiesTest extends BaseTemporalTest {

    @Autowired
    ConsumerActivity consumerActivity;

    @Autowired
    ProducerActivity producerActivity;

    TestActivityEnvironment env;

    @BeforeEach
    void setUp() {
        env = TestActivityEnvironment.newInstance();
        env.registerActivitiesImplementations(
                consumerActivity,
                producerActivity
        );
    }

    @Test
    @SuppressWarnings({"unchecked"})
    void testConsumer() throws InterruptedException {
        assertNotNull(consumerActivity);

        var activity = env.newActivityStub(ConsumerActivity.class);
        var heartbeats = mock(List.class);
        env.setActivityHeartbeatListener(Integer.class, heartbeats::add);

        // Start
        var run = Thread.ofVirtual().start(() -> {
            activity.consume("fake", 1);
        });

        // Wait for few heartbeats
        run.join(Duration.ofMillis(POLL_TIMEOUT_MS * 3));

        assertTrue(run.isAlive());
        verify(heartbeats, atLeastOnce()).add(anyInt());

        // Cancel
        env.requestCancelActivity();

        // Wait for finish
        Thread.sleep(POLL_TIMEOUT_MS * 3);
        assertFalse(run.isAlive());
    }

    @Test
    void testProducer() {
        assertNotNull(producerActivity);

        var activity = env.newActivityStub(ProducerActivity.class);
        assertDoesNotThrow(() -> activity.product("fake", 1));
    }
}
