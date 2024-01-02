package com.niit.kanbanService.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class User {
    @Id
    private String email;
    private String userName;
    private String password;
    private List<KanbanBoard> kanbanBoardList;

    public User() {
    }

    public User(String email, String userName, String password, List<KanbanBoard> kanbanBoardList) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.kanbanBoardList = kanbanBoardList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<KanbanBoard> getKanbanBoardList() {
        return kanbanBoardList;
    }

    public void setKanbanBoardList(List<KanbanBoard> kanbanBoardList) {
        this.kanbanBoardList = kanbanBoardList;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", kanbanBoardList=" + kanbanBoardList +
                '}';
    }
}
