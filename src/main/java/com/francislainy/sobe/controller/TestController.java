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
// todo: class to be removed once new valid endpoints are added - 06/11/2024
public class TestController {

    @GetMapping("/test-user")
    public ResponseEntity<Object> userRoleTest() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/test-admin")
    public ResponseEntity<Object> adminRoleTest() {
        return ResponseEntity.created(null).build();
    }
}
