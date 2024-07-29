package org.febit.demo.temporal.workflow.impl;

import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.febit.demo.temporal.config.ScheduleProducerProps;
import org.febit.demo.temporal.workflow.ScheduleService;
import org.febit.demo.temporal.workflow.api.ProducerActivity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ActivityImpl(taskQueues = ScheduleService.TASK_QUEUE)
@RequiredArgsConstructor
public class ProducerActivityImpl implements ProducerActivity {

    private final ScheduleProducerProps producerProps;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void product() {
        kafkaTemplate.send(producerProps.topic(), "hello");
    }
}
