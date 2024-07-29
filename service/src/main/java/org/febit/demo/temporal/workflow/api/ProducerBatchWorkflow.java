package org.febit.demo.temporal.workflow.api;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ProducerBatchWorkflow {

    @WorkflowMethod
    void batch();
}
