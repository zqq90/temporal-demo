package org.febit.demo.temporal.workflow.api;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.febit.demo.temporal.workflow.model.User;

@WorkflowInterface
public interface HelloWorkflow {

    @WorkflowMethod
    String sayHello(User person);
}
