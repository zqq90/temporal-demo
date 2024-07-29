package org.febit.demo.temporal.workflow;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.RequiredArgsConstructor;
import org.febit.demo.temporal.workflow.api.HelloWorkflow;
import org.febit.demo.temporal.workflow.model.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelloService {

    public static final String TASK_QUEUE = "Sample_Hello";

    private final WorkflowClient client;

    public String post(User person) {
        var workflow = client.newWorkflowStub(
                HelloWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue(TASK_QUEUE)
                        .setWorkflowId("HelloSample")
                        .build()
        );

        return workflow.sayHello(person);
    }
}
