package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.JwtAuthResponse;
import com.bbva.wallet.dtos.UserLogInDTO;
import com.bbva.wallet.dtos.UserSignUpDTO;
import com.bbva.wallet.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Registrar un usuario",description = "Registrar un usuario")
    @PostMapping("/register")
    public ResponseEntity<JwtAuthResponse> singUp (@Valid @RequestBody UserSignUpDTO userDto){
        return ResponseEntity.ok(authenticationService.signUp(userDto));
    }

    @Operation(summary = "Iniciar una sesion", description = "Iniciar una sesion")
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> LogIn (@Valid @RequestBody UserLogInDTO userDto){
        return ResponseEntity.ok(authenticationService.logIn(userDto));
    }
}

