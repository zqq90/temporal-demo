package org.febit.demo.temporal.workflow.impl;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import org.febit.demo.temporal.workflow.ScheduleDefs;
import org.febit.demo.temporal.workflow.ScheduleService;

import java.time.Duration;

@WorkflowImpl(taskQueues = ScheduleService.TASK_QUEUE)
public class ProducerWorkflowImpl implements ScheduleDefs.ProducerWorkflow {

    private final ScheduleDefs.ProducerActivity activity =
            Workflow.newActivityStub(
                    ScheduleDefs.ProducerActivity.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofMinutes(5))
                            .build()
            );

    @Override
    public boolean product(String batchId, int seq) {
        activity.product(batchId, seq);
        return true;
    }
}
