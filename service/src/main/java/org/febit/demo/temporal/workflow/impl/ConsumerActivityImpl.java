package org.febit.demo.temporal.workflow.impl;

import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.febit.demo.temporal.config.ScheduleConsumerProps;
import org.febit.demo.temporal.workflow.ScheduleService;
import org.febit.demo.temporal.workflow.api.ConsumerActivity;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@ActivityImpl(taskQueues = ScheduleService.TASK_QUEUE)
@RequiredArgsConstructor
public class ConsumerActivityImpl implements ConsumerActivity {

    private final ScheduleConsumerProps consumerProps;
    private final ConsumerFactory<String, String> consumerFactory;

    private final AtomicBoolean stopRequired = new AtomicBoolean(false);
    private final AtomicBoolean stopped = new AtomicBoolean(false);

    @Override
    public void requireStop() {
        log.info("NOTICE: stop required!!");
        stopRequired.set(true);
    }

    @Override
    public boolean isStopped() {
        // TODO graceful stop before terminated
        return stopped.get();
    }

    @Override
    public boolean consume() {
        // TODO consider consumer pool
        try (var consumer = consumerFactory.createConsumer()) {
            consumer.subscribe(consumerProps.topics());
            while (!stopRequired.get()) {
                var records = consumer.poll(Duration.ofMillis(900));
                records.forEach(this::process);
            }
        } finally {
            stopped.set(true);
        }
        return true;
    }

    private void process(ConsumerRecord<String, String> record) {
        log.info("NOTICE: offset = {}, key = {}, value = {}", record.offset(), record.key(), record.value());
    }

}
