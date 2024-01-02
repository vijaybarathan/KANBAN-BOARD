package com.niit.kanbanService.domain;

import org.springframework.data.annotation.Id;

import java.util.List;

public class KanbanStage {
    @Id
    private int stageId;
    private String stageName;
    private List<KanbanTask> kanbanTaskList;

    public KanbanStage() {
    }

    public KanbanStage(int stageId, String stageName, List<KanbanTask> kanbanTaskList) {
        this.stageId = stageId;
        this.stageName = stageName;
        this.kanbanTaskList = kanbanTaskList;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public List<KanbanTask> getKanbanTaskList() {
        return kanbanTaskList;
    }

    public void setKanbanTaskList(List<KanbanTask> kanbanTaskList) {
        this.kanbanTaskList = kanbanTaskList;
    }

    @Override
    public String toString() {
        return "KanbanStage{" +
                "stageId=" + stageId +
                ", stageName='" + stageName + '\'' +
                ", kanbanTaskList=" + kanbanTaskList +
                '}';
    }
}
