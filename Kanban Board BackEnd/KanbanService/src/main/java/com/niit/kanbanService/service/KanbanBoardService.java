package com.niit.kanbanService.service;

import com.niit.kanbanService.domain.KanbanBoard;
import com.niit.kanbanService.domain.KanbanStage;
import com.niit.kanbanService.domain.User;
import com.niit.kanbanService.exception.*;

import java.util.List;

public interface KanbanBoardService {

    //KanbanBoard
    List<User> getAllUsers() throws Exception;
    boolean deleteUser(String email) throws UserNotFoundException;
    User getAUser(String email) throws Exception;
    User registerUser(User user) throws UserAlreadyExistsException;
    User updateUser(String email,User user) throws UserNotFoundException;
    KanbanBoard saveUserKanbanBoardToBoardList(KanbanBoard kanbanBoard,String email) throws KanbanBoardAlreadyExistsException, UserNotFoundException;
    List<KanbanBoard> getAllKanbanBoardsFromBoardList(String email) throws Exception;
    User deleteKanbanBoard(String email,int boardId) throws KanbanBoardNotFoundException, UserNotFoundException;
    KanbanBoard getAKanbanBoardFromBoardList(String email,int boardId) throws KanbanBoardNotFoundException;
    User updateUserKanbanBoardListWithGivenKanbanBoard(String email,KanbanBoard kanbanBoard) throws UserNotFoundException, KanbanBoardNotFoundException;
    User addMemberToKanbanBoardByEmail(int boardId, String memberEmail,String userEmail)
            throws UserNotFoundException,KanbanBoardNotFoundException,TeamMemberAlreadyExistsException;
    List<String> getAllKanbanBoardNames(String email) throws KanbanBoardNotFoundException;
}
