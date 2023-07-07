package com.bbva.wallet.services;

import com.bbva.wallet.dtos.JwtAuthResponse;
import com.bbva.wallet.dtos.UserLogInDTO;
import com.bbva.wallet.dtos.UserSignUpDTO;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Role;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.enums.RoleName;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.RoleRepository;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Utils utils;

    @Value("${transaction.limit.ars}")
    private double transactionLimitArs;

    @Value("${transaction.limit.usd}")
    private double transactionLimitUsd;

    public JwtAuthResponse signUp(UserSignUpDTO userSignUpDto) {
        //Role role = new Role(RoleName.USER);

        var role = roleRepository.findByName
                        (userSignUpDto.getRole() != null ? userSignUpDto.getRole() : RoleName.USER)
                .orElse(Role.builder()
                        .name(RoleName.USER)
                        .description("Usuario")
                        .build());

        roleRepository.save(role);

        var user = User.builder()
                .firstName(userSignUpDto.getFirstName())
                .lastName(userSignUpDto.getLastName())
                .email(userSignUpDto.getEmail())
                .password(passwordEncoder.encode(userSignUpDto.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);
        Account accountPesos = Account.builder()
                .currency(Currency.ARS)
                .transactionLimit(transactionLimitArs)
                .balance(0.0)
                .user(user)
                .cbu(utils.generateRandomCbu())
                .build();
        Account accountDolares = Account.builder()
                .currency(Currency.USD)
                .transactionLimit(transactionLimitUsd)
                .balance(0.0)
                .user(user)
                .cbu(utils.generateRandomCbu())
                .build();

        accountRepository.save(accountPesos);
        accountRepository.save(accountDolares);

        var jwt = jwtService.generateToken(user);
        return JwtAuthResponse.builder().token(jwt).build();
    }

    public JwtAuthResponse logIn(UserLogInDTO userLogInDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLogInDTO.getEmail(), userLogInDTO.getPassword()));
        var user = userRepository.findByEmail(userLogInDTO.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));


        var jwt = jwtService.generateToken((user));
        return JwtAuthResponse.builder().token(jwt).build();
    }

}