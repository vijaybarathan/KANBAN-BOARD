package com.niit.notificationService.controller;

import com.niit.notificationService.domain.Message;
import com.niit.notificationService.domain.Notification;
import com.niit.notificationService.exception.MessageAlreadyExistsException;
import com.niit.notificationService.exception.MessageNotFoundException;
import com.niit.notificationService.exception.NotificationAlreadyExistsException;
import com.niit.notificationService.exception.NotificationNotFoundException;
import com.niit.notificationService.service.NotificationService;
import com.niit.notificationService.service.NotificationServiceImpl;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v3")
@CrossOrigin(origins = "*")
public class NotificationController {
    private NotificationServiceImpl notificationService;

    private ResponseEntity<?> responseEntity;
    @Autowired
    public NotificationController(NotificationServiceImpl notificationService){
        this.notificationService=notificationService;
    }

    @GetMapping("/kanban/getemailmsg")
    public ResponseEntity getMessagesByEmail(HttpServletRequest request){
        String email = getCustomerIdFromClaims(request);
        return new ResponseEntity(notificationService.getMessagesByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/kanban/getemailmsg/{msgId}")
    public ResponseEntity getMessagesByEmailWithMessageId(@PathVariable int msgId,HttpServletRequest request) throws MessageNotFoundException, NotificationNotFoundException {
        String email = getCustomerIdFromClaims(request);
        return new ResponseEntity(notificationService.getMessagesByEmailWithMessageId(msgId,email), HttpStatus.OK);
    }

    @PostMapping("/saveuser")
    public ResponseEntity<?> saveUser(@RequestBody Notification user) throws Exception {
        try {
            return new ResponseEntity<>(notificationService.saveUser(user), HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            throw new Exception();
        }
    }

    @PostMapping("kanban/savemessage")
    public ResponseEntity saveNotificationMessage(@RequestBody Message message, HttpServletRequest request) throws MessageAlreadyExistsException, NotificationNotFoundException {
        try {
            String email = getCustomerIdFromClaims(request);
            responseEntity = new ResponseEntity<>(notificationService.saveNotificationMessage(message,email), HttpStatus.CREATED);
        }
        catch (MessageAlreadyExistsException e)  {
            throw new MessageAlreadyExistsException();
        } catch (NotificationNotFoundException e) {
            throw new NotificationNotFoundException();
        }
        return responseEntity;
    }

    @PutMapping("kanban/updatemessage")
    public ResponseEntity updateNotificationMessage(@RequestBody Message message, HttpServletRequest request) throws NotificationNotFoundException,MessageNotFoundException {
        try {
            String email = getCustomerIdFromClaims(request);
            responseEntity = new ResponseEntity<>(notificationService.updateNotificationMessage(message,email), HttpStatus.CREATED);
        } catch (MessageNotFoundException e) {
            throw new MessageNotFoundException();
        } catch (NotificationNotFoundException e) {
            throw new NotificationNotFoundException();
        }
        return responseEntity;
    }

    @DeleteMapping("kanban/deletemessage/{msgId}")
    public ResponseEntity deleteNotificationMessage(@PathVariable int msgId, HttpServletRequest request) throws NotificationNotFoundException,MessageNotFoundException {
        try {
            String email = getCustomerIdFromClaims(request);
            responseEntity = new ResponseEntity<>(notificationService.deleteNotificationMessage(msgId, email), HttpStatus.CREATED);
        } catch (MessageNotFoundException e) {
            throw new MessageNotFoundException();
        } catch (NotificationNotFoundException e) {
            throw new NotificationNotFoundException();
        }
        return responseEntity;
    }

    private String getCustomerIdFromClaims(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        String email = (String) claims.get("email");
        return email;
    }



}
