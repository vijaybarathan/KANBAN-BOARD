package com.niit.notificationService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.CONFLICT, reason = "Message already Exists")
public class MessageAlreadyExistsException extends Exception {
}
