package org.febit.demo.temporal.workflow.impl;

import io.temporal.api.enums.v1.ParentClosePolicy;
import io.temporal.common.SearchAttributeKey;
import io.temporal.common.SearchAttributes;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Async;
import io.temporal.workflow.ChildWorkflowCancellationType;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.febit.demo.temporal.config.ScheduleConsumerProps;
import org.febit.demo.temporal.workflow.ScheduleDefs;
import org.febit.demo.temporal.workflow.ScheduleService;
import org.febit.demo.temporal.workflow.util.BatchUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.febit.demo.temporal.workflow.ScheduleService.BIZ_CONSUMER;
import static org.febit.demo.temporal.workflow.ScheduleService.BIZ_GROUP;
import static org.febit.demo.temporal.workflow.ScheduleService.KEY_BIZ;
import static org.febit.demo.temporal.workflow.ScheduleService.KEY_BIZ_BATCH_NUM;
import static org.febit.demo.temporal.workflow.ScheduleService.KEY_BIZ_GROUP;

@Slf4j
@Component
@RequiredArgsConstructor
@WorkflowImpl(taskQueues = ScheduleService.TASK_QUEUE)
public class ConsumerBatchWorkflowImpl implements ScheduleDefs.ConsumerBatchWorkflow {

    private final ScheduleConsumerProps consumerProps;

    @Override
    public void batch() {
        var batchId = BatchUtils.batchIdInMin();
        var batchNum = BatchUtils.batchNumberInMin();

        var options = ChildWorkflowOptions.newBuilder()
                .setTypedSearchAttributes(SearchAttributes.newBuilder()
                        .set(SearchAttributeKey.forText(KEY_BIZ_GROUP), BIZ_GROUP)
                        .set(SearchAttributeKey.forText(KEY_BIZ), BIZ_CONSUMER)
                        .set(SearchAttributeKey.forLong(KEY_BIZ_BATCH_NUM), batchNum)
                        .build()
                )
                .setParentClosePolicy(ParentClosePolicy.PARENT_CLOSE_POLICY_TERMINATE)
                .setCancellationType(ChildWorkflowCancellationType.WAIT_CANCELLATION_COMPLETED)
                .build();

        var promises = new ArrayList<Promise<Boolean>>();
        for (int i = 0; i < consumerProps.batchSize(); i++) {
            var child = Workflow.newChildWorkflowStub(ScheduleDefs.ConsumerWorkflow.class, options);
            var promise = Async.function(child::consume, batchId, i);
            promises.add(promise);
        }

        Promise.allOf(promises).get();
    }
}
