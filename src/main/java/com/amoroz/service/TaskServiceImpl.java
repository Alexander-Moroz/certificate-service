package com.amoroz.service;

import com.amoroz.entity.Status;
import com.amoroz.entity.Task;
import com.amoroz.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER = LogManager.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final StatusService statusService;
    private final NotificationService notificationService;
    private final JmsTemplate jmsTemplate;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, StatusService statusService, NotificationService notificationService, JmsTemplate jmsTemplate) {
        this.taskRepository = taskRepository;
        this.statusService = statusService;
        this.notificationService = notificationService;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public CompletableFuture<String> createTaskAndGetId(Task task) {
        task.setCreateDate(new Date(System.currentTimeMillis()));
        Status statusOpen = statusService.getStatusByName("CREATED");
        if (statusOpen != null) {
            task.setStatus(statusOpen);
            taskRepository.save(task);
            notificationService.notifyClient(task);
            jmsTemplate.convertAndSend("createdTasksQueue", task);
            return CompletableFuture.completedFuture(task.getId().toString());
        } else {
            LOGGER.error("ERROR processing task request: {}", task);
            return CompletableFuture.completedFuture("Can't process request. Try again.");
        }
    }

    @Override
    public String getTaskStatus(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        LOGGER.debug("getTaskStatus taskId: {}", taskId);
        if (optionalTask.isPresent()) {
            return optionalTask.get().getStatus().getName();
        }
        return null;
    }

    @Override
    public Optional<Task> getTask(Long taskId) {
        LOGGER.debug("getTask taskId: {}", taskId);
        return taskRepository.findById(taskId);
    }

    @Cacheable("base64cert")
    @Override
    public String getBase64Cert(Long taskId) throws JsonProcessingException {
        Optional<Task> taskOptional = getTask(taskId);
        LOGGER.debug("getBase64Cert taskId: {}", taskId);
        if (taskOptional.isPresent() && taskOptional.get().getStatus().equals(statusService.getStatusByName("CLOSED"))) {
            return encodeBase64(new ObjectMapper().writeValueAsString(taskOptional.get()));
        }
        return null;
    }

    private String encodeBase64(String s) {
        return Base64Utils.encodeToString(s.getBytes());
    }

    @Scheduled(fixedRate = 60000L)
    @CacheEvict(cacheNames = {"base64cert"})
    @Override
    public void clearBase64certCache() {
        LOGGER.debug("CLEAR base64cert cache");
    }

    @ExceptionHandler(JsonProcessingException.class)
    public String processException(JsonProcessingException e) {
        LOGGER.error("Exception: {}, cause: {}", e.getMessage(), e.getCause().toString());
        return "Error occurred. please write to admin@adm.in";
    }
}
