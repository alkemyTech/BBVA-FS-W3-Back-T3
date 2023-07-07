package com.bbva.wallet.services;

import com.bbva.wallet.dtos.UserLogInDTO;
import com.bbva.wallet.dtos.UserSignUpDTO;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Role;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.enums.RoleName;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.RoleRepository;
import com.bbva.wallet.utils.Utils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final Utils utils;


    public User signUp(UserSignUpDTO userSignUpDto) {
        //Role role = new Role(RoleName.USER);

        var role = roleRepository.findByName
                (userSignUpDto.getRoleName() != null ? userSignUpDto.getRoleName() : RoleName.USER)
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
                .transactionLimit(300_000.0)
                .balance(0.0)
                .user(user)
                .cbu(utils.generateRandomCbu())
                .build();
        Account accountDolares = Account.builder()
                .currency(Currency.USD)
                .transactionLimit(1_000.0)
                .balance(0.0)
                .user(user)
                .cbu(utils.generateRandomCbu())
                .build();

        accountRepository.save(accountPesos);
        accountRepository.save(accountDolares);

        return user;


    }

    public User logIn(UserLogInDTO userLogInDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLogInDTO.getEmail(), userLogInDTO.getPassword()));
        return userRepository.findByEmail(userLogInDTO.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    }
}
