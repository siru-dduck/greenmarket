package com.hanium.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @PostMapping("/users/join")
    public ResponseEntity<JoinResponse> joinUser(@RequestBody JoinRequest joinRequest) {
        return ResponseEntity.ok(null);
    }

}
