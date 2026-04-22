package com.learning.backend.api.messaging;

import com.learning.backend.common.messaging.TaskCreatedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskCreatedPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final TaskMessagingProperties properties;

    public TaskCreatedPublisher(RabbitTemplate rabbitTemplate, TaskMessagingProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    public void publish(TaskCreatedMessage message) {
        rabbitTemplate.convertAndSend(properties.getExchange(), properties.getRoutingKeyCreated(), message);
    }
}
