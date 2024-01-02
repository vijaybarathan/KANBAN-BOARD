package com.niit.kanbanService.domain;


import org.springframework.data.annotation.Id;

import java.util.Date;

public class KanbanTask {
    @Id
    private int taskId;
    private String taskTitle;
    private String taskDescription;
    private String taskAssignee;
    private Date startDate;
    private Date taskDeadline;
    private String taskPriority;
    private String taskType;

    public KanbanTask() {
    }

    public KanbanTask(int taskId, String taskTitle, String taskDescription, String taskAssignee, Date startDate, Date taskDeadline, String taskPriority, String taskType) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskAssignee = taskAssignee;
        this.startDate = startDate;
        this.taskDeadline = taskDeadline;
        this.taskPriority = taskPriority;
        this.taskType = taskType;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskAssignee() {
        return taskAssignee;
    }

    public void setTaskAssignee(String taskAssignee) {
        this.taskAssignee = taskAssignee;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(Date taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        return "KanbanTask{" +
                "taskId=" + taskId +
                ", taskTitle='" + taskTitle + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskAssignee='" + taskAssignee + '\'' +
                ", startDate=" + startDate +
                ", taskDeadline=" + taskDeadline +
                ", taskPriority='" + taskPriority + '\'' +
                ", taskType='" + taskType + '\'' +
                '}';
    }
}
