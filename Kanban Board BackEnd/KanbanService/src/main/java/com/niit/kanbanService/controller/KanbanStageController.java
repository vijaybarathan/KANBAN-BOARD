package com.niit.kanbanService.controller;

import com.niit.kanbanService.domain.KanbanStage;
import com.niit.kanbanService.exception.KanbanBoardNotFoundException;
import com.niit.kanbanService.exception.KanbanStageAlreadyExistsException;
import com.niit.kanbanService.exception.KanbanStageNotFoundException;
import com.niit.kanbanService.exception.UserNotFoundException;
import com.niit.kanbanService.service.KanbanStageService;
import com.niit.kanbanService.service.KanbanStageServiceImpl;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v2/user/stage")
@CrossOrigin(origins = "*")
public class KanbanStageController {
    private KanbanStageServiceImpl kanbanStageService;

    @Autowired
    public KanbanStageController(KanbanStageServiceImpl kanbanStageService){
        this.kanbanStageService=kanbanStageService;
    }

    @GetMapping("/getStages/{boardId}")
    public ResponseEntity<?> getAllStages(HttpServletRequest request, @PathVariable int boardId)
            throws UserNotFoundException, KanbanBoardNotFoundException {
        try {
            String email = getCustomerIdFromClaims(request);
            return new ResponseEntity(kanbanStageService.getAllStagesFromBoard(email, boardId), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (KanbanBoardNotFoundException e) {
            throw new KanbanBoardNotFoundException();
        }
    }

    @PostMapping("/addStages/{boardId}")
    public ResponseEntity<?> saveStagesToBoards(@PathVariable int boardId, @RequestBody KanbanStage stage,HttpServletRequest request)
            throws UserNotFoundException,KanbanBoardNotFoundException,KanbanStageAlreadyExistsException{
        try {
            String email = getCustomerIdFromClaims(request);
            return new ResponseEntity<>(kanbanStageService.saveStagesToBoards(email, boardId, stage), HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (KanbanBoardNotFoundException e) {
            throw new KanbanBoardNotFoundException();
        }
        catch (KanbanStageAlreadyExistsException e){
            throw new KanbanStageAlreadyExistsException();
        }
    }

    @DeleteMapping("/deletestage/{boardId}/{stageId}")
    public ResponseEntity<?> deleteStage(HttpServletRequest request, @PathVariable int boardId, @PathVariable int stageId)
            throws UserNotFoundException,KanbanBoardNotFoundException, KanbanStageNotFoundException {
        try {
            String email = getCustomerIdFromClaims(request);
            return new ResponseEntity<>(kanbanStageService.deleteStage(email, boardId, stageId), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (KanbanBoardNotFoundException e) {
            throw new KanbanBoardNotFoundException();
        } catch (KanbanStageNotFoundException e) {
            throw new KanbanStageNotFoundException();
        }
    }

    @PutMapping("/updatestages/{boardId}/{stageId}")
    public ResponseEntity<?> updateStages(HttpServletRequest request, @PathVariable int boardId, @PathVariable int stageId, @RequestBody KanbanStage kanbanStage)
            throws UserNotFoundException,KanbanStageNotFoundException,KanbanBoardNotFoundException{
        try {
            String email = getCustomerIdFromClaims(request);
            return new ResponseEntity<>(kanbanStageService.updateStage(email, boardId, stageId, kanbanStage), HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (KanbanBoardNotFoundException e) {
            throw new KanbanBoardNotFoundException();
        } catch (KanbanStageNotFoundException e) {
            throw new KanbanStageNotFoundException();
        }
    }

    @GetMapping("/getstagename/{boardId}")
    public ResponseEntity<?> getAllStageNames(HttpServletRequest request, @PathVariable int boardId) throws KanbanStageNotFoundException {
        try {
            String email = getCustomerIdFromClaims(request);
            return new ResponseEntity<>(kanbanStageService.getAllStageNames(email, boardId), HttpStatus.CREATED);
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
