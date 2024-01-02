package com.niit.kanbanService.service;

import com.niit.kanbanService.domain.KanbanTask;
import com.niit.kanbanService.domain.User;
import com.niit.kanbanService.exception.*;

import java.util.List;

public interface KanbanTaskService {
    KanbanTask getTaskByTaskName(String email, int boardId, int stageId, int taskId)
            throws UserNotFoundException, KanbanTaskNotFoundException;

    User saveTaskToStage(String email,int boardId, int stageId, KanbanTask task)
            throws UserNotFoundException, KanbanStageNotFoundException, KanbanTaskAlreadyExistsException, MoreThanTwoTasksException, KanbanBoardNotFoundException;

    User deleteTaskFromStage(String email,int boardId,int stageId,int taskId)
            throws UserNotFoundException, KanbanTaskNotFoundException, KanbanStageNotFoundException, KanbanBoardNotFoundException;

    User UpdateTask(String email,int boardId,int stageId,int taskId,KanbanTask task)
            throws UserNotFoundException, KanbanTaskNotFoundException, KanbanStageNotFoundException, KanbanBoardNotFoundException;

    List<KanbanTask> getAllTasks(String email, int stageId) throws Exception;

    User updatingTaskByDragAndDrop(String email,int boardId,int stageId,KanbanTask task) throws UserNotFoundException, MoreThanTwoTasksException, KanbanStageNotFoundException;
}
