package com.amoroz.service;

import com.amoroz.entity.Status;
import com.amoroz.entity.Task;
import com.amoroz.repository.TaskRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.util.Random;

@Component
public class TaskProcessorService {

    private static final Logger LOGGER = LogManager.getLogger(TaskProcessorService.class);

    private static final String[] endStatusNames = new String[] {"ERROR", "CLOSED"};
    private static final Random random = new Random();

    private final TaskRepository taskRepository;
    private final StatusService statusService;
    private final NotificationService notificationService;
    private final JmsTemplate jmsTemplate;

    @Autowired
    public TaskProcessorService(TaskRepository taskRepository, StatusService statusService, NotificationService notificationService, JmsTemplate jmsTemplate) {
        this.taskRepository = taskRepository;
        this.statusService = statusService;
        this.notificationService = notificationService;
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = "createdTasksQueue", concurrency = "5")
    public void processOpenTask(ObjectMessage objectMessage) throws JMSException {
        Task openTask = (Task) objectMessage.getObject();
        doSomeHardWork(openTask);
        Status status = statusService.getStatusByName("INPROGRESS");
        if (openTask != null && status != null) {
            openTask.setStatus(status);
            taskRepository.save(openTask);
            notificationService.notifyClient(openTask);
            jmsTemplate.convertAndSend("inProgressTasksQueue", openTask);
            LOGGER.debug("PROCESSED TASK {} status from CREATED to INPROGRESS", openTask.getId());
        } else {
            jmsTemplate.convertAndSend("unprocessedTasksQueue", openTask);
            LOGGER.error("received empty task/status: {}/{}", openTask, status);
        }
    }

    @JmsListener(destination = "inProgressTasksQueue", concurrency = "2")
    public void processInProgressTask(ObjectMessage objectMessage) throws JMSException {
        Task inProgressTask = (Task) objectMessage.getObject();
        doSomeHardWork(inProgressTask);
        Status status = statusService.getStatusByName(endStatusNames[random.nextInt(2)]);
        if (inProgressTask != null && status != null) {
            inProgressTask.setStatus(status);
            taskRepository.save(inProgressTask);
            notificationService.notifyClient(inProgressTask);
            LOGGER.debug("PROCESSED TASK {} status from INPROGRESS to {}", inProgressTask.getId(), status.getName());
        } else {
            jmsTemplate.convertAndSend("unprocessedTasksQueue", inProgressTask);
            LOGGER.error("received empty task/status: {}/{}", inProgressTask, status);
        }
    }

    private void doSomeHardWork(Task task) {
        LOGGER.debug("Some hard work for task: {}", task);
        try {
            Thread.sleep(random.nextInt(10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}