package com.learning.backend.domain.messaging;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "processed_events")
@IdClass(ProcessedEvent.ProcessedEventId.class)
public class ProcessedEvent {

    @Id
    @Column(name = "consumer_name", nullable = false, length = 100)
    private String consumerName;

    @Id
    @Column(name = "message_id", nullable = false, length = 200)
    private String messageId;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt = Instant.now();

    protected ProcessedEvent() {
    }

    public ProcessedEvent(String consumerName, String messageId) {
        this.consumerName = consumerName;
        this.messageId = messageId;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public String getMessageId() {
        return messageId;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public static final class ProcessedEventId implements Serializable {
        private String consumerName;
        private String messageId;

        public ProcessedEventId() {
        }

        public ProcessedEventId(String consumerName, String messageId) {
            this.consumerName = consumerName;
            this.messageId = messageId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ProcessedEventId that = (ProcessedEventId) o;
            return Objects.equals(consumerName, that.consumerName) && Objects.equals(messageId, that.messageId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(consumerName, messageId);
        }
    }
}
