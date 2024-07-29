package org.febit.demo.temporal.workflow.api;

public interface IStoppableWorkflow {

    void requireStop();

    boolean isStopped();
}
