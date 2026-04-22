package com.learning.backend.worker.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.backend.common.messaging.TaskTopology;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitWorkerConfiguration {

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    Declarables taskTopology() {
        TopicExchange exchange = new TopicExchange(TaskTopology.TASK_EVENTS_EXCHANGE, true, false);
        DirectExchange dlx = new DirectExchange(TaskTopology.TASK_DLX, true, false);
        Queue createdQueue = QueueBuilder.durable(TaskTopology.TASK_CREATED_QUEUE)
                .withArgument("x-dead-letter-exchange", TaskTopology.TASK_DLX)
                .withArgument("x-dead-letter-routing-key", TaskTopology.TASK_CREATED_DLQ_ROUTING_KEY)
                .build();
        Queue dlq = QueueBuilder.durable(TaskTopology.TASK_CREATED_DLQ).build();
        Binding toCreated = BindingBuilder.bind(createdQueue).to(exchange).with(TaskTopology.TASK_CREATED_ROUTING_KEY);
        Binding dlqBinding = BindingBuilder.bind(dlq).to(dlx).with(TaskTopology.TASK_CREATED_DLQ_ROUTING_KEY);
        return new Declarables(exchange, dlx, createdQueue, dlq, toCreated, dlqBinding);
    }
}
