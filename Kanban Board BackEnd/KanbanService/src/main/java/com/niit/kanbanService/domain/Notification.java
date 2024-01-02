package com.niit.kanbanService.domain;

import javax.persistence.Id;
import java.util.List;

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
}
