package org.febit.demo.temporal.config;

import io.temporal.spring.boot.WorkerOptionsCustomizer;
import io.temporal.worker.WorkerOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;

@Configuration
public class DemoWorkerOptionsCustomizer implements WorkerOptionsCustomizer {

    @Nonnull
    @Override
    public WorkerOptions.Builder customize(
            @Nonnull WorkerOptions.Builder builder,
            @Nonnull String workerName,
            @Nonnull String taskQueue
    ) {
        builder.setMaxConcurrentActivityExecutionSize(800);
        builder.setMaxConcurrentWorkflowTaskExecutionSize(800);
        builder.setMaxConcurrentLocalActivityExecutionSize(800);
        // builder.setMaxWorkerActivitiesPerSecond(40);
        // builder.setMaxTaskQueueActivitiesPerSecond(40);
        // builder.setMaxConcurrentActivityTaskPollers(40);
        // builder.setMaxConcurrentWorkflowTaskPollers(40);
        return builder;
    }
}
