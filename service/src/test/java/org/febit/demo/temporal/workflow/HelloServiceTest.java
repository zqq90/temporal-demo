package org.febit.demo.temporal.workflow;

import org.febit.demo.temporal.BaseTemporalTest;
import org.febit.demo.temporal.workflow.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HelloServiceTest extends BaseTemporalTest {

    @Autowired
    HelloService service;

    @Test
    public void testHello() {
        var result = service.post(new User("Tom"));

        assertThat(result)
                .isNotNull()
                .isEqualTo("Hello, Tom");
    }

}
