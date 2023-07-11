package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.JwtAuthResponse;
import com.bbva.wallet.dtos.UserLogInDTO;
import com.bbva.wallet.dtos.UserSignUpDTO;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<User> singUp (@Valid @RequestBody UserSignUpDTO userDto){
        return ResponseEntity.ok(authenticationService.signUp(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> LogIn (@Valid @RequestBody UserLogInDTO userDto){
        return ResponseEntity.ok(authenticationService.logIn(userDto));
    }

}
