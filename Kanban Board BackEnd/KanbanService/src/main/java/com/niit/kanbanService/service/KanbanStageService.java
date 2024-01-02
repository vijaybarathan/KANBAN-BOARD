package com.niit.kanbanService.service;

import com.niit.kanbanService.domain.KanbanStage;
import com.niit.kanbanService.domain.User;
import com.niit.kanbanService.exception.KanbanBoardNotFoundException;
import com.niit.kanbanService.exception.KanbanStageAlreadyExistsException;
import com.niit.kanbanService.exception.KanbanStageNotFoundException;
import com.niit.kanbanService.exception.UserNotFoundException;

import java.util.List;

public interface KanbanStageService {

    List<KanbanStage> getAllStagesFromBoard(String email, int boardId)throws UserNotFoundException, KanbanBoardNotFoundException;

    User saveStagesToBoards(String email,int boardId,KanbanStage stage) throws UserNotFoundException, KanbanBoardNotFoundException, KanbanStageAlreadyExistsException;

    boolean deleteStage(String email,int boardId,int stageId)throws UserNotFoundException, KanbanBoardNotFoundException, KanbanStageNotFoundException;

    User updateStage(String email, int boardId, int stageId, KanbanStage stage)
            throws UserNotFoundException,KanbanBoardNotFoundException, KanbanStageNotFoundException;
    List<String> getAllStageNames(String email,int boardId)throws KanbanStageNotFoundException;

}
