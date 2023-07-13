package com.bbva.wallet.services;

import com.bbva.wallet.dtos.JwtAuthResponse;
import com.bbva.wallet.dtos.UserLogInDTO;
import com.bbva.wallet.dtos.UserSignUpDTO;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.entities.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.bbva.wallet.entities.Role;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.enums.RoleName;
import com.bbva.wallet.repositories.RoleRepository;
import com.bbva.wallet.utils.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    private final Utils utils;
    @Value("${transaction.limit.ars}")
    private double transactionLimitArs;

    @Value("${transaction.limit.usd}")
    private double transactionLimitUsd;

    @Value("${initial.balance}")
    private double initialBalance;


    public JwtAuthResponse signUp(UserSignUpDTO userSignUpDto) {
        //Role role = new Role(RoleName.USER);

        var role = roleRepository.findByName

                        (userSignUpDto.getRoleName() != null ? userSignUpDto.getRoleName() : RoleName.USER)
                .orElse(com.bbva.wallet.entities.Role.builder()
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
                .balance(initialBalance)
                .user(user)
                .cbu(utils.generateRandomCbu())
                .build();
        Account accountDolares = Account.builder()
                .currency(Currency.USD)
                .transactionLimit(transactionLimitUsd)
                .balance(initialBalance)
                .user(user)
                .cbu(utils.generateRandomCbu())
                .build();

        accountRepository.save(accountPesos);
        accountRepository.save(accountDolares);

   


        accountService.createAccount(Currency.ARS, user);
        accountService.createAccount(Currency.USD, user);

        var jwt = jwtService.generateToken(user);
        return JwtAuthResponse.builder().token(jwt).user(user).build();

    }

    public JwtAuthResponse logIn(UserLogInDTO userLogInDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLogInDTO.getEmail(), userLogInDTO.getPassword()));
        var user = userRepository.findByEmail(userLogInDTO.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));


        var jwt = jwtService.generateToken(user);
        return JwtAuthResponse.builder().token(jwt).user(user).build();

    }

}