package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.JwtAuthResponse;
import com.bbva.wallet.dtos.UserLogInDTO;
import com.bbva.wallet.dtos.UserSignUpDTO;
import com.bbva.wallet.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<JwtAuthResponse> signUp(@Valid @RequestBody UserSignUpDTO userDto) {
        return ResponseEntity.ok(authenticationService.signUp(userDto));
    }

    @SneakyThrows
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> logIn(@Valid @RequestBody UserLogInDTO userDto) {
        return ResponseEntity.ok(authenticationService.logIn(userDto));
    }

    @SneakyThrows
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refresh(@RequestHeader("Authorization") String refreshToken) {
        JwtAuthResponse response = authenticationService.refreshToken(refreshToken);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build(); // Or return an appropriate error response
        }
    }
}
