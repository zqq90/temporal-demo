package org.febit.demo.temporal.workflow.impl;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.febit.demo.temporal.config.ScheduleProducerProps;
import org.febit.demo.temporal.workflow.ScheduleDefs;
import org.febit.demo.temporal.workflow.ScheduleService;
import org.febit.demo.temporal.workflow.model.Message;
import org.febit.lang.util.Logs;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ActivityImpl(taskQueues = ScheduleService.TASK_QUEUE)
@RequiredArgsConstructor
public class ProducerActivityImpl implements ScheduleDefs.ProducerActivity {

    private final ScheduleProducerProps producerProps;
    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final MeterRegistry meterRegistry;

    @Override
    public void product(String batchId, int seq) {
        var msg = Message.of(batchId, seq);
        log.info("NOTICE: product -> {}", Logs.json(msg));
        kafkaTemplate.send(producerProps.topic(), msg);

        Counter.builder("demo.message.send")
                .tag("batchId", msg.getBatchId())
                .register(meterRegistry)
                .increment();
    }
}
