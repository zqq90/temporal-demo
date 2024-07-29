package org.febit.demo.temporal.workflow.model;

public interface IScheduleProps {

    String workflowId();

    String scheduleId();

    String cron();

    Class<?> workflowType();
}
