package com.example.grpcreactivedemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    @GetMapping("/v1/health/status")
    public String checkStatus() {
        logger.info("Health check called");
        return "UP";
    }
}
