package org.febit.demo.temporal.workflow.api;

import io.temporal.activity.ActivityInterface;
import org.febit.demo.temporal.workflow.model.User;

@ActivityInterface
public interface HelloActivity {
    String hello(User person);
}
