package org.febit.demo.temporal.workflow.api;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ProducerWorkflow extends IStoppableWorkflow {

    @WorkflowMethod
    boolean product(String batchId, int seq);

    @SignalMethod
    void requireStop();

    @QueryMethod
    boolean isStopped();
}
