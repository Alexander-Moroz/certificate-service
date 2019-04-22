package com.amoroz.controller;

import com.amoroz.entity.Task;
import com.amoroz.service.StatusService;
import com.amoroz.service.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/cert")
public class CertController {

    private static final Logger LOGGER = LogManager.getLogger(CertController.class);

    private final TaskService taskService;
    private final StatusService statusService;

    @Autowired
    public CertController(TaskService taskService, StatusService statusService) {
        this.taskService = taskService;
        this.statusService = statusService;
    }

    @GetMapping
    public ResponseEntity<String> getCert(@RequestParam("id") Long taskId, HttpServletResponse response) throws IOException {
        LOGGER.debug("getCert id: {}", taskId);
        Optional<Task> taskOptional = taskService.getTask(taskId);
        if (taskOptional.isPresent() && taskOptional.get().getStatus().equals(statusService.getStatusByName("CLOSED"))) {
            String base64Cert = taskService.getBase64Cert(taskId);
            if (base64Cert != null) {
                return new ResponseEntity<>(base64Cert, HttpStatus.OK);
            } else {
                LOGGER.error(String.format("CERT IS NULL. taskId: %d", taskId));
                return new ResponseEntity<>("Error occurred. please write to admin@adm.in", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        LOGGER.warn(String.format("Cert is not ready. taskId: %d", taskId));
        response.sendRedirect("/task?id=" + taskId);
        return null;
    }
}