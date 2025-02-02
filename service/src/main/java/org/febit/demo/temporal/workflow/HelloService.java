package org.febit.demo.temporal.workflow;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.RequiredArgsConstructor;
import org.febit.demo.temporal.workflow.model.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelloService {

    public static final String TASK_QUEUE = "Sample_Hello";
    public static final String WF_ID = "HelloSample";

    private final WorkflowClient client;

    public String post(User person) {
        var workflow = client.newWorkflowStub(
                HelloDefs.HelloWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue(TASK_QUEUE)
                        .setWorkflowId(WF_ID)
                        .build()
        );

        return workflow.sayHello(person);
    }
}
