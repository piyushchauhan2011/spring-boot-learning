package com.learning.backend.api.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.messaging.tasks")
public class TaskMessagingProperties {

    private String exchange = "task.events";
    private String routingKeyCreated = "task.created";

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKeyCreated() {
        return routingKeyCreated;
    }

    public void setRoutingKeyCreated(String routingKeyCreated) {
        this.routingKeyCreated = routingKeyCreated;
    }
}
