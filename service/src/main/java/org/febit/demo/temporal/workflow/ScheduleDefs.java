package org.febit.demo.temporal.workflow;

import io.temporal.activity.ActivityInterface;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

public interface ScheduleDefs {

    @ActivityInterface
    interface ProducerActivity {

        void product(String batchId, int seq);
    }

    @WorkflowInterface
    interface ProducerBatchWorkflow {

        @WorkflowMethod
        void batch();
    }

    @WorkflowInterface
    interface ProducerWorkflow {

        @WorkflowMethod
        boolean product(String batchId, int seq);
    }

    @ActivityInterface
    interface ConsumerActivity {

        boolean consume(String batchId, int seq);
    }

    @WorkflowInterface
    interface ConsumerBatchWorkflow {

        @WorkflowMethod
        void batch();
    }

    @ActivityInterface
    interface ConsumersStopActivity {

        boolean request(long beforeBatchId);
    }

    @WorkflowInterface
    interface ConsumerWorkflow {

        @WorkflowMethod
        boolean consume(String batchId, int seq);
    }
}
