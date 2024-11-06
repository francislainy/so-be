package com.francislainy.sobe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/test-user")
    public ResponseEntity<Object> registerUser() {
        return ResponseEntity.created(null).build();
    }

    @PostMapping("/test-admin")
    public ResponseEntity<Object> loginUser() {
        return ResponseEntity.created(null).build();
    }
}