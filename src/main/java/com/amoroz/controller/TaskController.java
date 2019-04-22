package com.amoroz.controller;

import com.amoroz.entity.Task;
import com.amoroz.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {
    private static final Logger LOGGER = LogManager.getLogger(TaskController.class);

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(produces = "application/json;charset=utf8")
    public ResponseEntity createTask(@RequestBody @Valid Task task, BindingResult bindingResult) throws JsonProcessingException {
        String jsonTask = new ObjectMapper().writeValueAsString(task);
        LOGGER.info("createTask: PAYLOAD: {}", jsonTask);
        if (bindingResult.hasErrors()) {
            LOGGER.warn("task {} binding errors: {}", jsonTask, bindingResult.getAllErrors());
            return new ResponseEntity(
                    bindingResult.getAllErrors()
                            .stream()
                            .map(objectError -> {
                                if (objectError instanceof FieldError) {
                                    return String.format("%s: %s", ((FieldError)objectError).getField(), objectError.getDefaultMessage());
                                } else {
                                    return String.format("other: %s", objectError.getDefaultMessage());
                                }
                            })
                            .collect(Collectors.toList())
                        , HttpStatus.BAD_REQUEST);
        }
        CompletableFuture<String> futureId = taskService.createTaskAndGetId(task);
        String message = futureId.join();
        if (message.matches("\\d+")) {
            String responseMessage = String.format("Заявка принята. Идентификатор заявки %s", message);
            LOGGER.info(responseMessage);
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        } else {
            String responseMessage = String.format("Ошибка заявки %s", message);
            LOGGER.error("ERROR. task: {}, message: {}", task, responseMessage);
            return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<String> getTaskStatus(@RequestParam("id") Long taskId) {
        String taskStatus = taskService.getTaskStatus(taskId);
        String message = String.format("CertTaskStatus id: %d status: %s", taskId, taskStatus);
        LOGGER.info(message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}