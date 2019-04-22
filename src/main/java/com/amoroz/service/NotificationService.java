package com.amoroz.service;

import com.amoroz.entity.Task;
import com.amoroz.notification.Email;
import com.amoroz.notification.Sms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

@Service
public class NotificationService {

    private static final Logger LOGGER = LogManager.getLogger(NotificationService.class);

    private final JmsTemplate jmsTemplate;

    @Autowired
    public NotificationService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Async
    public void notifyClient(Task task) {
        String message = String.format("Task id: %d status: %s", task.getId(), task.getStatus().getName());
        if (task.getEmail() != null) {
            jmsTemplate.convertAndSend("allMessagesQueue", new Email(task.getEmail(), message), m -> {
                m.setStringProperty("key", "email");
                return m;
            });
            LOGGER.debug("SENT EMAIL MESSAGE TO QUEUE");
        }
        if (task.getPhoneNumber() != null) {
            jmsTemplate.convertAndSend("allMessagesQueue", new Sms(task.getPhoneNumber(), message), m -> {
                m.setStringProperty("key", "sms");
                return m;
            });
            LOGGER.debug("SENT SMS MESSAGE TO QUEUE");
        }
    }

    @JmsListener(destination = "allMessagesQueue", selector = "key='email'")
    public void processEmail(ObjectMessage objectMessage, @Header("key") String key) throws JMSException {
        if (objectMessage.getObject() != null && objectMessage.getObject() instanceof Email) {
            Email email = (Email) objectMessage.getObject();
            LOGGER.info("На {} отправлено уведомление: Новый статус {}", email.getTo(), email.getBody());
        }
    }

    @JmsListener(destination = "allMessagesQueue", selector = "key='sms'")
    public void processSms(ObjectMessage objectMessage, @Header("key") String key) throws JMSException {
        if (objectMessage.getObject() != null && objectMessage.getObject() instanceof Sms) {
            Sms sms = (Sms) objectMessage.getObject();
            LOGGER.info("На {} отправлено уведомление: Новый статус {}", sms.getPhoneNumber(), sms.getBody());
        }
    }
}