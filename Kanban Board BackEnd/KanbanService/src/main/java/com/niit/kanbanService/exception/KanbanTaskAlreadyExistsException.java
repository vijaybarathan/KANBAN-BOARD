package com.niit.kanbanService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT , reason = "KanbanTask already exists")
public class KanbanTaskAlreadyExistsException extends Exception {
}