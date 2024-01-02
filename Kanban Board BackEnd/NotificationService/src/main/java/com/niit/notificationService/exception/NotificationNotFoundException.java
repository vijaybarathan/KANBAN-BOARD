package com.niit.notificationService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Notification with specified id is not found")
public class NotificationNotFoundException extends Exception{
}
