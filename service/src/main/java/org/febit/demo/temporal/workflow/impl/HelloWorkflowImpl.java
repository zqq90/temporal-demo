package org.febit.demo.temporal.workflow.impl;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import org.febit.demo.temporal.workflow.HelloDefs;
import org.febit.demo.temporal.workflow.HelloService;
import org.febit.demo.temporal.workflow.model.User;

import java.time.Duration;

@WorkflowImpl(taskQueues = HelloService.TASK_QUEUE)
public class HelloWorkflowImpl implements HelloDefs.HelloWorkflow {

    private final HelloDefs.HelloActivity activity =
            Workflow.newActivityStub(
                    HelloDefs.HelloActivity.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofSeconds(2))
                            .build()
            );

    @Override
    public String sayHello(User person) {
        return activity.hello(person);
    }
}
