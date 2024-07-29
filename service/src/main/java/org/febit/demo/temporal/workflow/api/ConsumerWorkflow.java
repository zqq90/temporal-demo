package org.febit.demo.temporal.workflow.api;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ConsumerWorkflow extends IStoppableWorkflow {

    @WorkflowMethod
    boolean consume();

    @SignalMethod
    void requireStop();

    @QueryMethod
    boolean isStopped();
}
