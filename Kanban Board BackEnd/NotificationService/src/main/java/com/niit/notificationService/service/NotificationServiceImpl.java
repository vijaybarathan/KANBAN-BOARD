package com.niit.notificationService.service;

import com.niit.notificationService.domain.Message;
import com.niit.notificationService.domain.Notification;
import com.niit.notificationService.exception.MessageAlreadyExistsException;
import com.niit.notificationService.exception.MessageNotFoundException;
import com.niit.notificationService.exception.NotificationAlreadyExistsException;
import com.niit.notificationService.exception.NotificationNotFoundException;
import com.niit.notificationService.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationServiceImpl(NotificationRepository notificationRepository){
        this.notificationRepository=notificationRepository;
    }

    @Override
    public List<Message> getMessagesByEmail(String email) {
        List<Message> messages = notificationRepository.findById(email).get().getNotificationMessage();

        if (messages == null) {
            return new ArrayList<>();
        } else {
            return messages;
        }
    }

    @Override
    public Message getMessagesByEmailWithMessageId(int msgId, String email) throws NotificationNotFoundException, MessageNotFoundException {
        Notification notification = notificationRepository.findById(email).orElse(null);

        if (notification == null) {
            throw new NotificationNotFoundException();
        }

        List<Message> messageList = notification.getNotificationMessage();
        if (messageList == null || messageList.isEmpty()) {
            throw new MessageNotFoundException();
        }
        for (Message message : messageList){
            if (message.getMsgId() == msgId){
                return message;
            }
        }
        throw new MessageNotFoundException();
    }

    @Override
    public Notification saveNotificationMessage(Message message, String email) throws MessageAlreadyExistsException, NotificationNotFoundException {
        if (!notificationRepository.existsById(email)) {
            throw new NotificationNotFoundException();
        }
        Notification notification = notificationRepository.findById(email).get();
        if(notification.getNotificationMessage() == null)
        {
            notification.setNotificationMessage(Arrays.asList(message));
        }
        else {
            List<Message> messageList = notification.getNotificationMessage();
            for(Message msg : messageList){
                if(msg.getMsgId()==message.getMsgId()){
                    throw new MessageAlreadyExistsException();
                }
            }
            messageList.add(message);
        }
        return notificationRepository.save(notification);
    }

    @Override
    public Notification updateNotificationMessage(Message updatedMessage, String email) throws NotificationNotFoundException,MessageNotFoundException{
        if (!notificationRepository.existsById(email)) {
            throw new NotificationNotFoundException();
        }
        Notification notification = notificationRepository.findById(email).get();
        if(notification.getNotificationMessage() == null || notification.getNotificationMessage().isEmpty()){
            throw new MessageNotFoundException();
        } else {
            List<Message> messageList = notification.getNotificationMessage();
            boolean messageUpdated = false;

            for(int i=0;i< messageList.size();i++){
                Message message1 = messageList.get(i);
                if (message1.getMsgId()==(updatedMessage.getMsgId())){
                    messageList.set(i, updatedMessage);
                    messageUpdated =true;
                    break;
                }
            }
            if(!messageUpdated){
                throw new MessageNotFoundException();
            }
        }
        return notificationRepository.save(notification);
    }



    @Override
    public Notification deleteNotificationMessage(int msgId,String email) throws NotificationNotFoundException, MessageNotFoundException {

        if (!notificationRepository.existsById(email)) {
            throw new NotificationNotFoundException();
        }

        Notification notification = notificationRepository.findById(email).orElse(null);

        if (notification == null) {
            throw new NotificationNotFoundException();
        }

        List<Message> messageList = notification.getNotificationMessage();
        if (messageList == null || messageList.isEmpty()){
            throw new MessageNotFoundException();
        }
        boolean messageDeleted = false;
        for (Message message : messageList) {
            if (message.getMsgId() == msgId) {
                messageList.remove(message);
                messageDeleted = true;
                break;
            }
        }
        if (!messageDeleted){
            throw new MessageNotFoundException();
        }
        return notificationRepository.save(notification);
    }

    @Override
    public Notification saveUser(Notification user) throws Exception {
        if(notificationRepository.findById(user.getEmail()).isPresent())
        {
            throw new Exception();
        }
        else {
            user= notificationRepository.save(user);
        }
        return user;
    }
}
