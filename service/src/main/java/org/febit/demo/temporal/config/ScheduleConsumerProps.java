package org.febit.demo.temporal.config;

import jakarta.annotation.Nonnull;
import org.febit.demo.temporal.workflow.ScheduleDefs;
import org.febit.demo.temporal.workflow.model.IScheduleProps;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

import static org.febit.demo.temporal.workflow.ScheduleService.SCHEDULE_CONSUMERS;
import static org.febit.demo.temporal.workflow.ScheduleService.WF_CONSUMER_BATCH;

@ConfigurationProperties(prefix = "app.temporal.consumer")
public record ScheduleConsumerProps(
        @Nonnull
        String cron,
        int batchSize,
        @Nonnull
        List<String> topics
) implements IScheduleProps {

    @Override
    public String workflowId() {
        return WF_CONSUMER_BATCH;
    }

    @Override
    public String scheduleId() {
        return SCHEDULE_CONSUMERS;
    }

    @Override
    public Class<?> workflowType() {
        return ScheduleDefs.ConsumerBatchWorkflow.class;
    }
}
