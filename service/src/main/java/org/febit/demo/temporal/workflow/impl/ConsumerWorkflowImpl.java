package org.febit.demo.temporal.workflow.impl;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import org.febit.demo.temporal.workflow.ScheduleService;
import org.febit.demo.temporal.workflow.api.ConsumerActivity;
import org.febit.demo.temporal.workflow.api.ConsumerWorkflow;

import java.time.Duration;

@WorkflowImpl(taskQueues = ScheduleService.TASK_QUEUE)
public class ConsumerWorkflowImpl implements ConsumerWorkflow {

    private final ConsumerActivity consumer =
            Workflow.newActivityStub(
                    ConsumerActivity.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofMinutes(5))
                            .build()
            );

    @Override
    public boolean consume() {
        return consumer.consume();
    }

    @Override
    public void requireStop() {
        consumer.requireStop();
    }

    @Override
    public boolean isStopped() {
        return consumer.isStopped();
    }
}
