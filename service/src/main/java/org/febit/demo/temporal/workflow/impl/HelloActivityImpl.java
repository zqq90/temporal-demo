package org.febit.demo.temporal.workflow.impl;

import io.temporal.spring.boot.ActivityImpl;
import org.febit.demo.temporal.workflow.HelloService;
import org.febit.demo.temporal.workflow.api.HelloActivity;
import org.febit.demo.temporal.workflow.model.User;
import org.springframework.stereotype.Component;

@Component
@ActivityImpl(taskQueues = HelloService.TASK_QUEUE)
public class HelloActivityImpl implements HelloActivity {

    @Override
    public String hello(User user) {
        return "Hello, " + user.getName();
    }
}
