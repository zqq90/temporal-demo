package org.febit.demo.temporal.config;

import jakarta.annotation.Nonnull;
import org.febit.demo.temporal.workflow.ScheduleDefs;
import org.febit.demo.temporal.workflow.model.IScheduleProps;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.febit.demo.temporal.workflow.ScheduleService.SCHEDULE_PRODUCERS;
import static org.febit.demo.temporal.workflow.ScheduleService.WF_PRODUCER_BATCH;

@ConfigurationProperties(prefix = "app.temporal.producer")
public record ScheduleProducerProps(
        @Nonnull
        String cron,
        int batchSize,
        @Nonnull
        String topic
) implements IScheduleProps {

    @Override
    public String workflowId() {
        return WF_PRODUCER_BATCH;
    }

    @Override
    public String scheduleId() {
        return SCHEDULE_PRODUCERS;
    }

    @Override
    public Class<?> workflowType() {
        return ScheduleDefs.ProducerBatchWorkflow.class;
    }

}
