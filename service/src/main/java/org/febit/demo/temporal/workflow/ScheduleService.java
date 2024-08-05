package org.febit.demo.temporal.workflow;

import io.temporal.api.enums.v1.ScheduleOverlapPolicy;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.schedules.Schedule;
import io.temporal.client.schedules.ScheduleActionStartWorkflow;
import io.temporal.client.schedules.ScheduleClient;
import io.temporal.client.schedules.ScheduleDescription;
import io.temporal.client.schedules.ScheduleHandle;
import io.temporal.client.schedules.ScheduleOptions;
import io.temporal.client.schedules.SchedulePolicy;
import io.temporal.client.schedules.ScheduleSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.febit.demo.temporal.config.ScheduleConsumerProps;
import org.febit.demo.temporal.config.ScheduleProducerProps;
import org.febit.demo.temporal.workflow.model.IScheduleProps;
import org.febit.lang.func.Consumer2;
import org.febit.lang.util.Logs;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    public static final String TASK_QUEUE = "ScheduledKafka";

    public static final String KEY_BIZ = "Biz";
    public static final String KEY_BIZ_GROUP = "BizGroup";
    public static final String KEY_BIZ_BATCH_NUM = "BizBatchNumber";

    public static final String BIZ_GROUP = "ScheduledKafka";
    public static final String BIZ_PRODUCER = "producer";
    public static final String BIZ_CONSUMER = "consumer";

    public static final String WF_CONSUMER_BATCH = "WF_ConsumerBatch";
    public static final String SCHEDULE_CONSUMERS = "Schedule_Consumers";

    public static final String WF_PRODUCER_BATCH = "WF_ProducerBatch";
    public static final String SCHEDULE_PRODUCERS = "Schedule_Producers";

    private final ScheduleConsumerProps consumerProps;
    private final ScheduleProducerProps producerProps;

    private final ScheduleClient scheduleClient;

    public void startAll() {
        startSchedule(consumerProps);
        startSchedule(producerProps);
    }

    public void stopAll() {
        stopScheduleShadow(SCHEDULE_CONSUMERS);
        stopScheduleShadow(SCHEDULE_PRODUCERS);
    }

    public Map<String, Object> metrics() {
        var metrics = new LinkedHashMap<String, Object>();
        scheduleMetrics(SCHEDULE_CONSUMERS, (key, value) ->
                metrics.put("Consumers Schedule" + key, value)
        );
        scheduleMetrics(SCHEDULE_PRODUCERS, (key, value) ->
                metrics.put("Producers Schedule" + key, value)
        );
        return metrics;
    }

    private void scheduleMetrics(String id, Consumer2<String, Object> consumer) {
        ScheduleHandle schedule;
        try {
            schedule = scheduleClient.getHandle(id);
        } catch (Exception e) {
            consumer.accept("Error", e.getMessage());
            return;
        }

        var desc = schedule.describe();
        var info = desc.getInfo();
        var nextTimes = info.getNextActionTimes();
        var recentActions = info.getRecentActions();

        var recent = recentActions.isEmpty() ? null : recentActions.getLast();

        consumer.accept("Running", true);
        consumer.accept("Running Actions", info.getRunningActions().size());
        consumer.accept("LastUpdatedAt", info.getLastUpdatedAt());
        consumer.accept("Next Time", nextTimes.isEmpty() ? "--" : nextTimes.getFirst());

        if (recent != null) {
            consumer.accept("Recent Action", recent.getStartedAt());

            var secs = Duration.between(recent.getStartedAt(), Instant.now()).toSeconds();

            consumer.accept("Recent Action Seconds to Now", secs);

            // TODO: hard code: 2 minutes
            consumer.accept("Recent Action Health", secs > 2 * 60 ? "Bad" : "Good");
        } else {
            consumer.accept("Recent Action", "---");
        }

    }

    private void stopScheduleShadow(String scheduleId) {
        var schedule = scheduleClient.getHandle(scheduleId);

        ScheduleDescription desc;
        try {
            desc = schedule.describe();
        } catch (Exception e) {
            log.warn("Failed to describe schedule", e);
            return;
        }

        log.debug("Schedule: {}", Logs.json(desc));
        try {
            schedule.delete();
        } catch (Exception e) {
            log.warn("Failed to delete schedule", e);
        }
    }

    private ScheduleHandle startSchedule(IScheduleProps props) {
        var schedule = Schedule.newBuilder()
                .setPolicy(SchedulePolicy.newBuilder()
                        .setOverlap(ScheduleOverlapPolicy.SCHEDULE_OVERLAP_POLICY_TERMINATE_OTHER)
                        .build()
                )
                .setSpec(ScheduleSpec.newBuilder()
                        .setCronExpressions(List.of(
                                props.cron()
                        ))
                        .build()
                )
                .setAction(ScheduleActionStartWorkflow.newBuilder()
                        .setWorkflowType(props.workflowType())
                        .setArguments()
                        .setOptions(
                                WorkflowOptions.newBuilder()
                                        .setTaskQueue(TASK_QUEUE)
                                        .setWorkflowId(props.workflowId())
                                        .build()
                        )
                        .build()
                )
                .build();

        return scheduleClient.createSchedule(props.scheduleId(), schedule,
                ScheduleOptions.newBuilder().build()
        );
    }
}
