package org.febit.demo.temporal.workflow;

import io.temporal.activity.ActivityInterface;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.febit.demo.temporal.workflow.model.User;

public interface HelloDefs {
    @WorkflowInterface
    interface HelloWorkflow {

        @WorkflowMethod
        String sayHello(User person);
    }

    @ActivityInterface
    interface HelloActivity {
        String hello(User person);
    }
}
