package com.bbva.wallet.services;

import com.bbva.wallet.dtos.UserLogInDTO;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.bbva.wallet.dtos.JwtAuthResponse;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public User logIn(UserLogInDTO userLogInDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLogInDTO.getEmail(), userLogInDTO.getPassword()));
        return  userRepository.findByEmail(userLogInDTO.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    }
}
