package com.niit.kanbanService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niit.kanbanService.domain.KanbanBoard;
import com.niit.kanbanService.domain.User;
import com.niit.kanbanService.exception.*;
import com.niit.kanbanService.service.KanbanBoardService;
import com.niit.kanbanService.service.KanbanBoardServiceImpl;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v2")
@CrossOrigin(origins = "*")
public class KanbanController {
    private KanbanBoardServiceImpl kanbanService;
    private ResponseEntity<?> responseEntity;
    @Autowired
    public KanbanController(KanbanBoardServiceImpl kanbanService){
        this.kanbanService=kanbanService;
    }
    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws UserAlreadyExistsException {

        try {
            responseEntity =  new ResponseEntity<>(kanbanService.registerUser(user), HttpStatus.CREATED);
        }
        catch(UserAlreadyExistsException e)
        {
            throw new UserAlreadyExistsException();
        }
        return responseEntity;
    }

    @PostMapping("user/kanbanBoard")
    public ResponseEntity<?> saveKanbanBoard(@RequestBody KanbanBoard kanbanBoard, HttpServletRequest request) throws UserNotFoundException, KanbanBoardAlreadyExistsException {

        try {
            String email = getCustomerIdFromClaims(request);
            responseEntity = new ResponseEntity<>(kanbanService.saveUserKanbanBoardToBoardList(kanbanBoard, email), HttpStatus.CREATED);
        }
        catch (UserNotFoundException e)
        {
            throw new UserNotFoundException();
        }
        return responseEntity;
    }

    @DeleteMapping("user/delete")
    public ResponseEntity<?> deleteUser(HttpServletRequest request)throws UserNotFoundException{
        try {
            String email = getCustomerIdFromClaims(request);
            System.out.println("Email ID: " + email);
            kanbanService.deleteUser(email);
            return new ResponseEntity("DELETED", HttpStatus.OK);
        }catch (UserNotFoundException e){
            throw new UserNotFoundException();
        }
    }

    @PutMapping("user/update")
    public ResponseEntity<?> updateUser(HttpServletRequest request,@RequestBody User user)throws UserNotFoundException{
        try {
            String email = getCustomerIdFromClaims(request);
            System.out.println("Email ID: " + email);
            return new ResponseEntity(kanbanService.updateUser(email,user), HttpStatus.OK);
        }catch (UserNotFoundException e){
            throw new UserNotFoundException();
        }
    }

    @GetMapping("/user/getuser")
    public ResponseEntity<?> getAUser(HttpServletRequest request) {
        try {
            String email = getCustomerIdFromClaims(request);
            return new ResponseEntity<>(kanbanService.getAUser(email), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("user/kanbanBoards")
    public ResponseEntity<?> getAllKanbanBoards(HttpServletRequest request) throws KanbanBoardNotFoundException {
        try{
            String email = getCustomerIdFromClaims(request);
            return new ResponseEntity<>(kanbanService.getAllKanbanBoardsFromBoardList(email), HttpStatus.OK);
        }catch(Exception e)
        {
            throw new KanbanBoardNotFoundException();
        }
    }

    @GetMapping("user/kanbanBoards/{boardId}")
    public ResponseEntity<?> getAKanbanBoardFromBoardList(@PathVariable int boardId,HttpServletRequest request){

        try{
            String email = getCustomerIdFromClaims(request);
            responseEntity = new ResponseEntity(kanbanService.getAKanbanBoardFromBoardList(email,boardId), HttpStatus.OK);

        }catch (Exception exception){
            responseEntity = new ResponseEntity(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PostMapping("/user/addboard_members/{boardId}/{memberEmail}")
    public ResponseEntity<?> addBoardMember(@PathVariable int boardId,@PathVariable String memberEmail,HttpServletRequest request)
            throws UserNotFoundException, JsonProcessingException, TeamMemberAlreadyExistsException, KanbanBoardNotFoundException {
        String email = getCustomerIdFromClaims(request);
        try{
            return new ResponseEntity<>(kanbanService.addMemberToKanbanBoardByEmail(boardId,memberEmail,email),HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (KanbanBoardNotFoundException e) {
            throw new KanbanBoardNotFoundException();
        } catch (TeamMemberAlreadyExistsException m) {
            throw new TeamMemberAlreadyExistsException();
        }
    }

    @DeleteMapping("user/kanbanBoard/{boardId}")
    public ResponseEntity<?> deleteKanbanBoard(@PathVariable int boardId,HttpServletRequest request) throws KanbanBoardNotFoundException {

        String email = getCustomerIdFromClaims(request);
        try {
            return new ResponseEntity<>(kanbanService.deleteKanbanBoard(email, boardId), HttpStatus.OK);
        } catch (UserNotFoundException | KanbanBoardNotFoundException m) {
            throw new KanbanBoardNotFoundException();
        }
    }

    @PutMapping("user/kanbanBoard")
    public ResponseEntity<?> updateKanbanBoard(@RequestBody KanbanBoard kanbanBoard, HttpServletRequest request) throws KanbanBoardNotFoundException {

        String email = getCustomerIdFromClaims(request);
        try {
            return new ResponseEntity<>(kanbanService.updateUserKanbanBoardListWithGivenKanbanBoard(email, kanbanBoard), HttpStatus.OK);
        } catch (UserNotFoundException | KanbanBoardNotFoundException m) {
            throw new KanbanBoardNotFoundException();
        }
    }

    @GetMapping("/user/getboardnames")
    public ResponseEntity<?> getNamesOfBoards(HttpServletRequest request)throws KanbanBoardNotFoundException{
        String email = getCustomerIdFromClaims(request);
        try {
            return new ResponseEntity<>(kanbanService.getAllKanbanBoardNames(email),HttpStatus.OK);
        }catch (KanbanBoardNotFoundException e){
            throw new KanbanBoardNotFoundException();
        }
    }

    @GetMapping("/user/allusers")
    public ResponseEntity<?> getAllUsers() throws Exception{
        try {
            return new ResponseEntity<>(kanbanService.getAllUsers(), HttpStatus.OK);
        } catch (Exception exception){
            return new ResponseEntity(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getCustomerIdFromClaims(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        String email = (String) claims.get("email");
        return email;
    }
}
