package org.febit.demo.temporal.workflow.impl;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import org.febit.demo.temporal.workflow.ScheduleService;
import org.febit.demo.temporal.workflow.api.ProducerActivity;
import org.febit.demo.temporal.workflow.api.ProducerWorkflow;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

@WorkflowImpl(taskQueues = ScheduleService.TASK_QUEUE)
public class ProducerWorkflowImpl implements ProducerWorkflow {

    private final ProducerActivity activity =
            Workflow.newActivityStub(
                    ProducerActivity.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofMinutes(5))
                            .build()
            );

    private final AtomicBoolean stopRequired = new AtomicBoolean(false);
    private final AtomicBoolean stopped = new AtomicBoolean(false);

    @Override
    public boolean product() {
        activity.product();
        return true;
    }

    @Override
    public void requireStop() {
        stopRequired.set(true);
    }

    @Override
    public boolean isStopped() {
        return stopped.get();
    }
}
