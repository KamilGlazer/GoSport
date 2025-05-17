package com.kamilglazer.gosport.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendNotification(CommentNotificationPayload payload) {
        rabbitTemplate.convertAndSend("notificationsQueue", payload);
    }
}
