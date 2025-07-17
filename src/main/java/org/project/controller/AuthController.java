package org.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.validation.BindingResult;
import org.project.DTO.UserRegistrationDto;
import org.project.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signupUser(@Valid @RequestBody UserRegistrationDto registrationDto, 
                             BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().toString());
        }   

        try {
            userService.register(registrationDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signinUser(@Valid @RequestBody UserRegistrationDto registrationDto, 
                             BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid signin data");
        }
        
        try {
            userService.login(registrationDto);
            return ResponseEntity.ok("User signed in successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }

}