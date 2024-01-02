package com.niit.kanbanService.domain;

import org.springframework.data.annotation.Id;

public class Message {
    @Id
    private int msgId;

    private String message;


    public Message(int msgId, String message) {
        this.msgId = msgId;
        this.message = message;
    }

    public Message() {
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msgId=" + msgId +
                ", message='" + message + '\'' +
                '}';
    }
}
