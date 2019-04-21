package com.amoroz.service;

import com.amoroz.entity.Task;
import com.amoroz.repository.TaskRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;

@Component
public class TaskProcessorService {

    private static final Logger LOGGER = LogManager.getLogger(TaskProcessorService.class);

    private final TaskRepository taskRepository;
    private final NotificationService notificationService;
    private final JmsTemplate jmsTemplate;

    private static final Random random = new Random();

    @Autowired
    public TaskProcessorService(TaskRepository taskRepository, NotificationService notificationService, JmsTemplate jmsTemplate) {
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
        this.jmsTemplate = jmsTemplate;
    }

    //todo variable time(?QUEUE)
    @Scheduled(fixedDelay = 5000L)
    public void processOpenTask() {
        Optional<Task> optionalTask = taskRepository.findFirstByStatus(1);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setStatus(2);
            taskRepository.save(task);
            notificationService.notifyClient(task);
            LOGGER.debug("PROCESSED TASK {} status from 1 to {}", task.getId(), task.getStatus());
        }
    }

    //todo variable time
    @Scheduled(fixedDelay = 15000L)
    public void processInProgressTask() {
        Optional<Task> optionalTask = taskRepository.findFirstByStatus(2);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setStatus(new Random().nextInt(2) + 3);
            taskRepository.save(task);
            notificationService.notifyClient(task);
            LOGGER.debug("PROCESSED TASK {} status from 2 to {}", task.getId(), task.getStatus());
        }
    }
}