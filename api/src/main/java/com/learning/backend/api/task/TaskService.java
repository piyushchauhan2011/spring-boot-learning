package com.learning.backend.api.task;

import com.learning.backend.api.messaging.TaskCreatedPublisher;
import com.learning.backend.common.api.CreateTaskRequest;
import com.learning.backend.common.api.PageResponse;
import com.learning.backend.common.api.TaskResponse;
import com.learning.backend.common.api.UpdateTaskRequest;
import com.learning.backend.common.messaging.TaskCreatedMessage;
import com.learning.backend.domain.task.Task;
import com.learning.backend.domain.task.TaskRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static com.learning.backend.api.config.CacheConfiguration.TASKS_BY_ID_CACHE;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskCreatedPublisher taskCreatedPublisher;

    public TaskService(TaskRepository taskRepository, TaskCreatedPublisher taskCreatedPublisher) {
        this.taskRepository = taskRepository;
        this.taskCreatedPublisher = taskCreatedPublisher;
    }

    @Transactional
    public TaskResponse create(CreateTaskRequest request) {
        Task task = new Task(request.title(), request.description());
        Task saved = taskRepository.save(task);
        String messageId = UUID.randomUUID().toString();
        taskCreatedPublisher.publish(new TaskCreatedMessage(
                saved.getId(),
                saved.getTitle(),
                Instant.now(),
                messageId
        ));
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = TASKS_BY_ID_CACHE, key = "#id")
    public TaskResponse get(Long id) {
        return taskRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<TaskResponse> list(int page, int size) {
        Page<Task> result = taskRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
        return new PageResponse<>(
                result.map(this::toResponse).getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Transactional
    @CacheEvict(cacheNames = TASKS_BY_ID_CACHE, key = "#id")
    public TaskResponse update(Long id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.setTitle(request.title());
        task.setDescription(request.description());
        return toResponse(taskRepository.save(task));
    }

    @Transactional
    @CacheEvict(cacheNames = TASKS_BY_ID_CACHE, key = "#id")
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
