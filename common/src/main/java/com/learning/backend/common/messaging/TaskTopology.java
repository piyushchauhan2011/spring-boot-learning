package com.learning.backend.common.messaging;

public final class TaskTopology {

    public static final String TASK_EVENTS_EXCHANGE = "task.events";
    public static final String TASK_CREATED_ROUTING_KEY = "task.created";
    public static final String TASK_CREATED_QUEUE = "task.created";
    public static final String TASK_DLX = "task.dlx";
    public static final String TASK_CREATED_DLQ = "task.created.dlq";
    public static final String TASK_CREATED_DLQ_ROUTING_KEY = "task.created.dlq";

    private TaskTopology() {
    }
}
