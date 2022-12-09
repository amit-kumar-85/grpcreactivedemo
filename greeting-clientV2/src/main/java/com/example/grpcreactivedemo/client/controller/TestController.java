package com.example.grpcreactivedemo.client.controller;

import com.example.grpcreactivedemo.client.enums.CallType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.grpcreactivedemo.client.service.TestService;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping(value = "/greeting")
    public String greeting(@RequestParam String message, @RequestParam CallType type) {
        return testService.greeting(message, type);
    }
}
