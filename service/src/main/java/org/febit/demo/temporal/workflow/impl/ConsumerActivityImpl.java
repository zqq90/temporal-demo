package org.febit.demo.temporal.workflow.impl;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.temporal.activity.Activity;
import io.temporal.client.ActivityCompletionException;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.febit.demo.temporal.config.ScheduleConsumerProps;
import org.febit.demo.temporal.workflow.ScheduleDefs;
import org.febit.demo.temporal.workflow.ScheduleService;
import org.febit.demo.temporal.workflow.model.Message;
import org.febit.lang.util.Logs;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@ActivityImpl(taskQueues = ScheduleService.TASK_QUEUE)
@RequiredArgsConstructor
public class ConsumerActivityImpl implements ScheduleDefs.ConsumerActivity {

    private final ScheduleConsumerProps consumerProps;
    private final ConsumerFactory<String, Message> consumerFactory;
    private final MeterRegistry meterRegistry;

    @Override
    public boolean consume(String batchId, int seq) {
        Counter.builder("demo.consumer.started")
                .tag("batchId", batchId)
                .register(meterRegistry)
                .increment();

        var ctx = Activity.getExecutionContext();

        try (var consumer = consumerFactory.createConsumer()) {
            consumer.subscribe(consumerProps.topics());

            for (int i = 0; ; i++) {
                try {
                    ctx.heartbeat(i);
                } catch (ActivityCompletionException e) {
                    log.info("Consumer for [{}] ({}) finished", batchId, seq);
                    return true;
                }

                var records = consumer.poll(Duration.ofMillis(900));
                if (records.isEmpty()) {
                    continue;
                }

                Counter.builder("demo.consumer.message")
                        .tag("batchId", batchId)
                        .tag("seq", String.valueOf(seq))
                        .register(meterRegistry)
                        .increment(records.count());
                records.forEach(this::process);
            }
        } finally {
            Counter.builder("demo.consumer.stopped")
                    .tag("batchId", batchId)
                    .register(meterRegistry)
                    .increment();
        }
    }

    private void process(ConsumerRecord<String, Message> record) {
        var msg = record.value();
        log.info("NOTICE: offset = {}, {}", record.offset(), Logs.json(msg));
        Counter.builder("demo.message.processed")
                .tag("batchId", msg.getBatchId())
                .register(meterRegistry)
                .increment();
    }

}
