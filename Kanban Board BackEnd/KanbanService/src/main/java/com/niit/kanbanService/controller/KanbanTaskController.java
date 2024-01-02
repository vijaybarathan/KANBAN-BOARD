package com.niit.kanbanService.controller;

import com.niit.kanbanService.domain.KanbanTask;
import com.niit.kanbanService.exception.*;
import com.niit.kanbanService.service.KanbanTaskService;
import com.niit.kanbanService.service.KanbanTaskServiceImpl;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v2/user/task")
@CrossOrigin(origins = "*")
public class KanbanTaskController {

    private KanbanTaskServiceImpl kanbanTaskService;

    @Autowired
    public KanbanTaskController(KanbanTaskServiceImpl kanbanTaskService){
        this.kanbanTaskService=kanbanTaskService;
    }

    @GetMapping("gettask/{boardId}/{stageId}/{taskId}")
    public ResponseEntity<?> getTaskByTaskName(HttpServletRequest request, @PathVariable int boardId, @PathVariable int stageId, @PathVariable int taskId)
            throws UserNotFoundException, KanbanTaskNotFoundException {
        try {
            String email = getCustomerIdFromClaims(request);
            return new ResponseEntity(kanbanTaskService.getTaskByTaskName(email, boardId, stageId, taskId), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (KanbanTaskNotFoundException e) {
            throw new KanbanTaskNotFoundException();
        }
    }

    @PostMapping("/savetask/{boardId}/{stageId}")
    public ResponseEntity<?> saveTasks(HttpServletRequest request,@PathVariable int boardId, @PathVariable int stageId, @RequestBody KanbanTask task)
            throws UserNotFoundException, KanbanStageNotFoundException, KanbanTaskAlreadyExistsException, MoreThanTwoTasksException,KanbanBoardNotFoundException {
        try {
            String email = getCustomerIdFromClaims(request);
            return new ResponseEntity<>(kanbanTaskService.saveTaskToStage(email, boardId, stageId, task), HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (KanbanTaskAlreadyExistsException e) {
            return new ResponseEntity<>("Task Already Exists", HttpStatus.CONFLICT);
        } catch (KanbanStageNotFoundException e) {
            return new ResponseEntity<>("Stage not found", HttpStatus.NOT_FOUND);
        } catch (MoreThanTwoTasksException e) {
            throw new MoreThanTwoTasksException();
        }
    }

    @DeleteMapping("/deletetask/{boardId}/{stageId}/{taskId}")
    public ResponseEntity<?> deleteTask(HttpServletRequest request, @PathVariable int boardId, @PathVariable int stageId, @PathVariable int taskId)
            throws UserNotFoundException,KanbanTaskNotFoundException,KanbanStageNotFoundException{
        try {
            String email = getCustomerIdFromClaims(request);
            return new ResponseEntity(kanbanTaskService.deleteTaskFromStage(email, boardId, stageId, taskId), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (KanbanTaskNotFoundException e) {
            throw new KanbanTaskNotFoundException();
        } catch (KanbanStageNotFoundException e) {
            throw new KanbanStageNotFoundException();
        } catch (KanbanBoardNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/updatetask/{boardId}/{stageId}/{taskId}")
    public ResponseEntity<?> updateTheTask(HttpServletRequest request, @PathVariable int boardId,
                                        @PathVariable int stageId, @PathVariable int taskId, @RequestBody KanbanTask task)
            throws UserNotFoundException,KanbanTaskNotFoundException,KanbanStageNotFoundException{
        try {
            String email = getCustomerIdFromClaims(request);
            return new ResponseEntity<>(kanbanTaskService.UpdateTask(email, boardId, stageId, taskId, task), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (KanbanTaskNotFoundException e) {
            throw new KanbanTaskNotFoundException();
        } catch (KanbanStageNotFoundException e) {
            throw new KanbanStageNotFoundException();
        } catch (KanbanBoardNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getalltasks/{stageId}")
    public ResponseEntity<?> getAllTasks(HttpServletRequest request, @PathVariable int stageId) {
        try {
            String email = getCustomerIdFromClaims(request);
            return new ResponseEntity<>(kanbanTaskService.getAllTasks(email, stageId), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @PostMapping("/updatedraganddrop/{boardId}/{stageId}")
    public ResponseEntity<?> updatingTaskByDragAndDrop(HttpServletRequest request,@PathVariable int boardId,@PathVariable int stageId,@RequestBody KanbanTask task) throws UserNotFoundException, MoreThanTwoTasksException, KanbanStageNotFoundException {
        try {
            String email = getCustomerIdFromClaims(request);
            return new ResponseEntity<>(kanbanTaskService.updatingTaskByDragAndDrop(email,boardId,stageId,task),HttpStatus.CREATED);
        } catch (UserNotFoundException e ){
            throw new UserNotFoundException();
        } catch (KanbanStageNotFoundException e) {
            throw new KanbanStageNotFoundException();
        }
    }

    private String getCustomerIdFromClaims(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        String email = (String) claims.get("email");
        return email;
    }
}
