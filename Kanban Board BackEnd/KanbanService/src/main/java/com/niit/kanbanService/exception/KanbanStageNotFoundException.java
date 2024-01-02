package com.niit.kanbanService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR , reason = "KanbanStage Not exists")
public class KanbanStageNotFoundException extends Exception {
}