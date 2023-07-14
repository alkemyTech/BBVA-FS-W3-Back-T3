package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.JwtAuthResponse;
import com.bbva.wallet.dtos.UserLogInDTO;
import com.bbva.wallet.dtos.UserSignUpDTO;
import com.bbva.wallet.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<JwtAuthResponse> singUp (@Valid @RequestBody UserSignUpDTO userDto){
        return ResponseEntity.ok(authenticationService.signUp(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> LogIn (@Valid @RequestBody UserLogInDTO userDto){
        return ResponseEntity.ok(authenticationService.logIn(userDto));
    }
}

