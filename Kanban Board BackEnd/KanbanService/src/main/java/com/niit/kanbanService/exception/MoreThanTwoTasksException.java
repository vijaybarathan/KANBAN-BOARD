package com.niit.kanbanService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR , reason = "Member should not have more than two tasks while in Wip")
public class MoreThanTwoTasksException extends Exception {
}
