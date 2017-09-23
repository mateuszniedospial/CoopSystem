package com.coopsystem.web.socket.notification;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by Dariusz Ł on 14.05.2017.
 */
@Service
public class NotificationSender {
    private final SimpMessagingTemplate messagingTemplate;
    private String destination;

    public NotificationSender(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void send(Notification notification, String destination) {
        messagingTemplate.convertAndSend(destination, notification);
    }
}
