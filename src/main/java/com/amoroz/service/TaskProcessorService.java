package com.amoroz.service;

import com.amoroz.entity.Status;
import com.amoroz.entity.Task;
import com.amoroz.repository.StatusRepository;
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
    private final StatusRepository statusRepository;
    private final NotificationService notificationService;
    private final JmsTemplate jmsTemplate;

    private static final Random random = new Random();

    @Autowired
    public TaskProcessorService(TaskRepository taskRepository, StatusRepository statusRepository, NotificationService notificationService, JmsTemplate jmsTemplate) {
        this.taskRepository = taskRepository;
        this.statusRepository = statusRepository;
        this.notificationService = notificationService;
        this.jmsTemplate = jmsTemplate;
    }

    //todo variable time(?QUEUE)
    @Scheduled(fixedDelay = 5000L)
    public void processOpenTask() {
        Optional<Status> optionalStatus = statusRepository.findById(1);
        if (optionalStatus.isPresent()) {
            Optional<Task> optionalTask = taskRepository.findFirstByStatus(optionalStatus.get());
            if (optionalTask.isPresent()) {
                Task task = optionalTask.get();
                optionalStatus = statusRepository.findById(2);
                if (optionalStatus.isPresent()) {
                    task.setStatus(optionalStatus.get());
                    taskRepository.save(task);
                    notificationService.notifyClient(task);
                    LOGGER.debug("PROCESSED TASK {} status from 1 to {}", task.getId(), task.getStatus());
                } else {
                    LOGGER.error("ERROR PROCESSING TASK {} status from 1 to 2. Status is not present", task.getId());
                }
            }
        } else {
            LOGGER.error("ERROR PROCESSING TASK/ Status 1 is not present");
        }
    }

    //todo variable time
    @Scheduled(fixedDelay = 15000L)
    public void processInProgressTask() {
        Optional<Status> optionalStatus = statusRepository.findById(2);
        if (optionalStatus.isPresent()) {
            Optional<Task> optionalTask = taskRepository.findFirstByStatus(optionalStatus.get());
            if (optionalTask.isPresent()) {
                Task task = optionalTask.get();
                int nextStatusId = new Random().nextInt(2) + 3;
                optionalStatus = statusRepository.findById(nextStatusId);
                if (optionalStatus.isPresent()) {
                    task.setStatus(optionalStatus.get());
                    taskRepository.save(task);
                    notificationService.notifyClient(task);
                    LOGGER.debug("PROCESSED TASK {} status from 2 to {}", task.getId(), task.getStatus());
                } else {
                    LOGGER.error("ERROR PROCESSING TASK {} status from 2 to %d. Status is not present", task.getId(), nextStatusId);
                }
            }
        } else {
            LOGGER.error("ERROR PROCESSING TASK/ Status 2 is not present");
        }
    }
}