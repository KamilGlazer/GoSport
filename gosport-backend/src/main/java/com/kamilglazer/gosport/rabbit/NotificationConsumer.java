package com.kamilglazer.gosport.rabbit;


import com.kamilglazer.gosport.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationService notificationService;

    @RabbitListener(queues = "notificationsQueue")
    public void handleNotification(CommentNotificationPayload payload) {
        notificationService.createNotificationFromComment(payload);
    }
}
