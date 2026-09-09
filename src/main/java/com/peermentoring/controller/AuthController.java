package com.peermentoring.controller;

import com.peermentoring.dto.LoginRequest;
import com.peermentoring.dto.RegisterRequest;
import com.peermentoring.dto.UserDTO;
import com.peermentoring.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController handles authentication endpoints for Registration and Login.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Allows local development frontend calls
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest request) {
        UserDTO userDTO = authService.registerUser(request);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    /**
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequest request) {
        UserDTO userDTO = authService.loginUser(request);
        return ResponseEntity.ok(userDTO);
    }
}
