package org.febit.demo.temporal.workflow;

import io.temporal.api.enums.v1.IndexedValueType;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.schedules.ScheduleClient;
import io.temporal.testing.TestEnvironmentOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.febit.demo.temporal.BaseTemporalTest;
import org.febit.demo.temporal.config.ScheduleConsumerProps;
import org.febit.demo.temporal.config.ScheduleProducerProps;
import org.febit.demo.temporal.workflow.ScheduleDefs.ConsumerActivity;
import org.febit.demo.temporal.workflow.ScheduleDefs.ConsumerBatchWorkflow;
import org.febit.demo.temporal.workflow.ScheduleDefs.ProducerActivity;
import org.febit.demo.temporal.workflow.ScheduleDefs.ProducerBatchWorkflow;
import org.febit.demo.temporal.workflow.impl.ConsumerBatchWorkflowImpl;
import org.febit.demo.temporal.workflow.impl.ConsumerWorkflowImpl;
import org.febit.demo.temporal.workflow.impl.ProducerBatchWorkflowImpl;
import org.febit.demo.temporal.workflow.impl.ProducerWorkflowImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.febit.demo.temporal.workflow.ScheduleService.KEY_BIZ;
import static org.febit.demo.temporal.workflow.ScheduleService.KEY_BIZ_BATCH_NUM;
import static org.febit.demo.temporal.workflow.ScheduleService.KEY_BIZ_GROUP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ScheduleServiceTest extends BaseTemporalTest {

    final ConsumerActivity consumerActivity = mock(ConsumerActivity.class);
    final ProducerActivity producerActivity = mock(ProducerActivity.class);

    @Autowired
    ScheduleConsumerProps consumerProps;
    @Autowired
    ScheduleProducerProps producerProps;

    TestWorkflowEnvironment env;
    ScheduleService scheduleService;
    ScheduleClient scheduleClient;
    WorkflowClient client;
    Worker worker;

    @BeforeEach
    void setUp() {
        env = TestWorkflowEnvironment.newInstance(TestEnvironmentOptions.newBuilder()
                .registerSearchAttribute(KEY_BIZ, IndexedValueType.INDEXED_VALUE_TYPE_TEXT)
                .registerSearchAttribute(KEY_BIZ_GROUP, IndexedValueType.INDEXED_VALUE_TYPE_TEXT)
                .registerSearchAttribute(KEY_BIZ_BATCH_NUM, IndexedValueType.INDEXED_VALUE_TYPE_INT)
                .build()
        );

        worker = env.newWorker(ScheduleService.TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(
                ProducerWorkflowImpl.class,
                ConsumerWorkflowImpl.class
        );

        worker.registerWorkflowImplementationFactory(
                ConsumerBatchWorkflow.class,
                () -> new ConsumerBatchWorkflowImpl(consumerProps)
        );
        worker.registerWorkflowImplementationFactory(
                ProducerBatchWorkflow.class,
                () -> new ProducerBatchWorkflowImpl(producerProps)
        );
        worker.registerActivitiesImplementations(
                consumerActivity,
                producerActivity
        );

        env.start();

        client = env.getWorkflowClient();
        scheduleClient = ScheduleClient.newInstance(env.getWorkflowServiceStubs());
        scheduleService = new ScheduleService(
                consumerProps,
                producerProps,
                scheduleClient
        );
        reset(consumerActivity, producerActivity);
    }

    @Test
    void testConsumers() {
        var workflow = client.newWorkflowStub(
                ConsumerBatchWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue(ScheduleService.TASK_QUEUE)
                        .build()
        );

        assertDoesNotThrow(workflow::batch);

        verify(consumerActivity, times(consumerProps.batchSize()))
                .consume(any(), anyInt());

//        // TODO: FAILED: UNIMPLEMENTED: Method temporal.api.workflowservice.v1.WorkflowService/ListWorkflowExecutions is unimplemented
//        var batchWfs = listExecutions("WorkflowType = \""
//                + ConsumerBatchWorkflow.class.getSimpleName() + "\"");
//        assertThat(batchWfs).hasSize(1);
//
//        // TODO: FAILED: UNIMPLEMENTED: Method temporal.api.workflowservice.v1.WorkflowService/ListWorkflowExecutions is unimplemented
//        var consumerWfs = client.listExecutions("WorkflowType = \""
//                        + ConsumerWorkflowImpl.class.getSimpleName() + "\"")
//                .toList();
//        assertThat(consumerWfs)
//                .hasSize(consumerProps.batchSize());
    }

    @Test
    void testProducers() {
        var workflow = client.newWorkflowStub(
                ProducerBatchWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue(ScheduleService.TASK_QUEUE)
                        .build()
        );

        assertDoesNotThrow(workflow::batch);

        verify(producerActivity, times(producerProps.batchSize()))
                .product(any(), anyInt());
    }

//    // TODO: FAILED: UNIMPLEMENTED: Method temporal.api.workflowservice.v1.WorkflowService/ListWorkflowExecutions is unimplemented
//    private List<WorkflowExecutionInfo> listExecutions(String query) {
//        return env.getWorkflowServiceStubs()
//                .blockingStub()
//                .listWorkflowExecutions(ListWorkflowExecutionsRequest.newBuilder()
//                        .setNamespace(client.getOptions().getNamespace())
//                        .setQuery(query)
//                        .build())
//                .getExecutionsList();
//    }
//
//    @Test
//    void testSchedule() {
//        // TODO: FAILED: UNIMPLEMENTED: Method temporal.api.workflowservice.v1.WorkflowService/CreateSchedule is unimplemented
//        scheduleService.startAll();
//    }

}
