package org.febit.demo.temporal.workflow.impl;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import org.febit.demo.temporal.workflow.ScheduleDefs;
import org.febit.demo.temporal.workflow.ScheduleService;

import java.time.Duration;

@WorkflowImpl(taskQueues = ScheduleService.TASK_QUEUE)
public class ConsumerWorkflowImpl implements ScheduleDefs.ConsumerWorkflow {

    private final ScheduleDefs.ConsumerActivity consumer =
            Workflow.newActivityStub(
                    ScheduleDefs.ConsumerActivity.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofMinutes(5))
                            .build()
            );

    @Override
    public boolean consume(String batchId, int seq) {
        return consumer.consume(batchId, seq);
    }
}
