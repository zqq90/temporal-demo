package org.febit.demo.temporal.config;

import io.temporal.client.WorkflowClient;
import io.temporal.client.schedules.ScheduleClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.testing.TestWorkflowEnvironment;
import org.febit.demo.temporal.workflow.HelloService;
import org.febit.demo.temporal.workflow.impl.HelloActivityImpl;
import org.febit.demo.temporal.workflow.impl.HelloWorkflowImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockTemporal {

    @Bean
    public TestWorkflowEnvironment testWorkflowEnvironment() {
        var env = TestWorkflowEnvironment.newInstance();

        var worker = env.newWorker(HelloService.TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(
                HelloWorkflowImpl.class
        );
        worker.registerActivitiesImplementations(
                new HelloActivityImpl()
        );
        env.start();
        return env;
    }

    @Bean
    public WorkflowClient workflowClient() {
        var env = testWorkflowEnvironment();
        return env.getWorkflowClient();
    }

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        var env = testWorkflowEnvironment();
        return env.getWorkflowServiceStubs();
    }

    @Bean
    public ScheduleClient scheduleClient() {
        return ScheduleClient.newInstance(workflowServiceStubs());
    }
}
