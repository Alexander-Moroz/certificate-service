package com.amoroz.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cert")
public class CertController {
    private static final Logger LOGGER = LogManager.getLogger(CertController.class);

    @PostMapping
    public ResponseEntity<String> createCertRequest() {
        LOGGER.debug("createCertRequest: OK");
        return new ResponseEntity<>("createCertRequest: OK", HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<String> getCert(@RequestParam("id") String certId) {
        LOGGER.debug("getCert: OK. Cert id: {}", certId);
        return new ResponseEntity<>("getCert: OK. Cert id: " + certId, HttpStatus.OK);
    }

    @GetMapping("/task{id}")
    public ResponseEntity<String> getStatus(@RequestParam("id") String taskId) {
        LOGGER.debug("getStatus: status. Task id: {}", taskId);
        return new ResponseEntity<>("getStatus: status. Task id: " + taskId, HttpStatus.OK);
    }
}