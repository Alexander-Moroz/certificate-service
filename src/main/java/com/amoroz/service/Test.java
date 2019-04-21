package com.amoroz.service;

import com.amoroz.notification.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.jms.*;

//TODO TEST
//@Service
public class Test {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue allMessagesQueue;

    @Scheduled(fixedDelay = 10000L)
    public void produceAndGetResponse() throws JMSException {
        Email email = new Email("Eto", "Ebody");
        Email finalEmail = email;
        System.out.println(String.format("SENDING TEST EMAIL(%s, %s)", email.getTo(), email.getBody()));
        Message message = jmsTemplate.sendAndReceive(allMessagesQueue, session -> {
            Message m = session.createObjectMessage(finalEmail);
            m.setStringProperty("key", "test");
            return m;
        });
        System.out.println(String.format("SENT TEST EMAIL(%s, %s)", email.getTo(), email.getBody()));

        ObjectMessage resp = (ObjectMessage) message;
        if (resp == null) {
            System.err.println("FAILED TO RECEIVE TEST BACK EMAIL. TRY LATER");
        } else if (resp.getObject() != null && resp.getObject() instanceof Email) {
            email = (Email) resp.getObject();
            System.out.println(String.format("RECEIVED TEST BACK EMAIL(%s, %s)", email.getTo(), email.getBody()));
        }
    }

    @JmsListener(destination = "allMessagesQueue", selector = "key='test'")
    public Message replyToTest(ObjectMessage objectMessage, Session session) throws JMSException, InterruptedException {
        System.out.println("RECEIVED TEST MESSAGE");
        if (objectMessage != null && objectMessage.getObject() != null && objectMessage.getObject() instanceof Email) {
            Email email = (Email) objectMessage.getObject();
            email.setTo("MODIFIED_" + email.getTo());
            email.setBody("MODIFIED_" + email.getBody());
            System.out.println("PROCESSED TEST EMAIL");
            return session.createObjectMessage(email);
        }
        return objectMessage;
    }

}