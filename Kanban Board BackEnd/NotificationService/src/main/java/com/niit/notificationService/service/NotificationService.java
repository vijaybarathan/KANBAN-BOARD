package com.niit.notificationService.service;

import com.niit.notificationService.domain.Message;
import com.niit.notificationService.domain.Notification;
import com.niit.notificationService.exception.MessageAlreadyExistsException;
import com.niit.notificationService.exception.MessageNotFoundException;
import com.niit.notificationService.exception.NotificationAlreadyExistsException;
import com.niit.notificationService.exception.NotificationNotFoundException;

import java.util.List;

public interface NotificationService {

    List<Message> getMessagesByEmail(String email);

    Message getMessagesByEmailWithMessageId(int msgId,String email) throws NotificationNotFoundException, MessageNotFoundException;
    Notification saveNotificationMessage(Message message,String email) throws MessageAlreadyExistsException,NotificationNotFoundException;

    Notification deleteNotificationMessage(int msgId, String email) throws NotificationNotFoundException,MessageNotFoundException;
    Notification updateNotificationMessage(Message message,String email) throws NotificationNotFoundException, MessageNotFoundException;

    Notification saveUser(Notification user) throws Exception;
}
