package com.bbva.wallet.services;


import com.bbva.wallet.dtos.UserSignUpDTO;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;




    public User signUp (UserSignUpDTO userSignUpDto) {
       var user = User.builder()
               .firstName(userSignUpDto.getFirstName())
               .lastName(userSignUpDto.getLastName())
               .email(userSignUpDto.getEmail())
               .password((passwordEncoder.encode(userSignUpDto.getPassword())))
               .build();

       userRepository.save(user);


        return user;


    }



}