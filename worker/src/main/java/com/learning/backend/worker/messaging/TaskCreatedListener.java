package com.learning.backend.worker.messaging;

import com.learning.backend.common.messaging.TaskCreatedMessage;
import com.learning.backend.common.messaging.TaskTopology;
import com.learning.backend.domain.messaging.ProcessedEvent;
import com.learning.backend.domain.messaging.ProcessedEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TaskCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(TaskCreatedListener.class);
    private static final String CONSUMER_NAME = "worker-task-created-v1";

    private final ProcessedEventRepository processedEventRepository;
    private final JavaMailSender mailSender;

    public TaskCreatedListener(ProcessedEventRepository processedEventRepository, JavaMailSender mailSender) {
        this.processedEventRepository = processedEventRepository;
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = TaskTopology.TASK_CREATED_QUEUE)
    @Transactional
    public void onTaskCreated(TaskCreatedMessage message) {
        var id = new ProcessedEvent.ProcessedEventId(CONSUMER_NAME, message.messageId());
        if (processedEventRepository.existsById(id)) {
            log.debug("Duplicate message {}, skipping", message.messageId());
            return;
        }
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("worker@localhost");
        mail.setTo("tasks@localhost");
        mail.setSubject("New task: " + message.title());
        mail.setText("Task id=%d created at %s (messageId=%s)".formatted(
                message.taskId(),
                message.occurredAt(),
                message.messageId()
        ));
        try {
            mailSender.send(mail);
        } catch (Exception ex) {
            log.warn("Mail send failed for task {}: {}", message.taskId(), ex.getMessage());
        }
        processedEventRepository.save(new ProcessedEvent(CONSUMER_NAME, message.messageId()));
    }
}
