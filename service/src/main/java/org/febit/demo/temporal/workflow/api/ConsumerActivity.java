package org.febit.demo.temporal.workflow.api;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface ConsumerActivity {

    boolean consume();

    void requireStop();

    boolean isStopped();
}
