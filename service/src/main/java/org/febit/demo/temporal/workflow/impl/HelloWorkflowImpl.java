package org.febit.demo.temporal.workflow.impl;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import org.febit.demo.temporal.workflow.HelloService;
import org.febit.demo.temporal.workflow.api.HelloActivity;
import org.febit.demo.temporal.workflow.api.HelloWorkflow;
import org.febit.demo.temporal.workflow.model.User;

import java.time.Duration;

@WorkflowImpl(taskQueues = HelloService.TASK_QUEUE)
public class HelloWorkflowImpl implements HelloWorkflow {

    private final HelloActivity activity =
            Workflow.newActivityStub(
                    HelloActivity.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofSeconds(2))
                            .build()
            );

    @Override
    public String sayHello(User person) {
        return activity.hello(person);
    }
}
