package com.niit.notificationService.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Notification {
    @Id
    private String email;
    private List<Message> notificationMessage;

    public Notification() {
    }

    public Notification(String email, List<Message> notificationMessage) {
        this.email = email;
        this.notificationMessage = notificationMessage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Message> getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(List<Message> notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "email='" + email + '\'' +
                ", notificationMessage=" + notificationMessage +
                '}';
    }
}
