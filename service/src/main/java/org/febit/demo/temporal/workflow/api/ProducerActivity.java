package org.febit.demo.temporal.workflow.api;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface ProducerActivity {

    void product();
}
