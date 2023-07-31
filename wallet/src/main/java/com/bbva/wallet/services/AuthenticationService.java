package com.bbva.wallet.services;

import com.bbva.wallet.dtos.JwtAuthResponse;
import com.bbva.wallet.dtos.UserLogInDTO;
import com.bbva.wallet.dtos.UserSignUpDTO;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.UserException;
import com.bbva.wallet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.bbva.wallet.entities.Role;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.enums.RoleName;
import com.bbva.wallet.repositories.RoleRepository;
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
        accountService.createAccount(Currency.ARS, user);
        accountService.createAccount(Currency.USD, user);

        return jwtService.generateToken(user);
    }

    public JwtAuthResponse logIn(UserLogInDTO userLogInDTO) throws UserException {

        var user = userRepository.findByEmail(userLogInDTO.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLogInDTO.getEmail(), userLogInDTO.getPassword()));
        } catch (BadCredentialsException e) {
            var message = (user.isEmpty()) ? "User not found" : "Bad login credentials";
            var errorCode = (user.isEmpty()) ? ErrorCodes.USER_NOT_FOUND : ErrorCodes.BAD_CREDENTIALS;
            throw new UserException(message, errorCode);
        }

        return jwtService.generateToken(user.get());

    }

    public JwtAuthResponse refreshToken(String refreshToken) throws UserException {
        String username = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserException("User not found", ErrorCodes.USER_NOT_FOUND));

        return jwtService.generateToken(user);
    }
}
