package com.niit.kanbanService.domain;


import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class KanbanBoard {
    @Id
    private int boardId;
    private String boardName;
    private String boardOwnerName;
    private String boardDescription;
    private String boardCreatedOn;
    private List<String> boardMembers;
    private List<KanbanStage> kanbanStageList;

    public KanbanBoard() {
    }

    public KanbanBoard(int boardId, String boardName, String boardOwnerName, String boardDescription, String boardCreatedOn, List<String> boardMembers, List<KanbanStage> kanbanStageList) {
        this.boardId = boardId;
        this.boardName = boardName;
        this.boardOwnerName = boardOwnerName;
        this.boardDescription = boardDescription;
        this.boardCreatedOn = boardCreatedOn;
        this.boardMembers = boardMembers;
        this.kanbanStageList = kanbanStageList;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardOwnerName() {
        return boardOwnerName;
    }

    public void setBoardOwnerName(String boardOwnerName) {
        this.boardOwnerName = boardOwnerName;
    }

    public String getBoardDescription() {
        return boardDescription;
    }

    public void setBoardDescription(String boardDescription) {
        this.boardDescription = boardDescription;
    }

    public String getBoardCreatedOn() {
        return boardCreatedOn;
    }

    public void setBoardCreatedOn(String boardCreatedOn) {
        this.boardCreatedOn = boardCreatedOn;
    }

    public List<String> getBoardMembers() {
        return boardMembers;
    }

    public void setBoardMembers(List<String> boardMembers) {
        this.boardMembers = boardMembers;
    }

    public List<KanbanStage> getKanbanStageList() {
        return kanbanStageList;
    }

    public void setKanbanStageList(List<KanbanStage> kanbanStageList) {
        this.kanbanStageList = kanbanStageList;
    }

    @Override
    public String toString() {
        return "KanbanBoard{" +
                "boardId=" + boardId +
                ", boardName='" + boardName + '\'' +
                ", boardOwnerName='" + boardOwnerName + '\'' +
                ", boardDescription='" + boardDescription + '\'' +
                ", boardCreatedOn=" + boardCreatedOn +
                ", boardMembers=" + boardMembers +
                ", kanbanStageList=" + kanbanStageList +
                '}';
    }
}
