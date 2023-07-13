package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.UserEditDTO;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.TransactionException;
import com.bbva.wallet.exeptions.UserException;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Objects;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.getId ")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id ) {
        userService.softDeleteById(id);
        accountService.softDeleteByUserId(id);
        return ResponseEntity.noContent().build();
    }

    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<User> allUsers(@PathVariable("id") Long id, Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        User user= userService.findById(id).orElseThrow(() -> new TransactionException("No existe el usuario indicado ", ErrorCodes.USER_DOESNT_EXIST));
        if (!Objects.equals(user.getId(), userLoggedIn.getId())){
            throw new UserException("No se puede ver un usuario ajeno ", ErrorCodes.USER_DOESNT_EXIST);
        }

        return ResponseEntity.ok(user);
    }

    @SneakyThrows
    @PatchMapping("/{id}")
    public ResponseEntity<User> editUser(@PathVariable("id") Long id, @RequestBody UserEditDTO userEditDTO, Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        User user= userService.findById(id).orElseThrow(() -> new TransactionException("No existe el usuario indicado ", ErrorCodes.USER_DOESNT_EXIST));
        if (!Objects.equals(user.getId(), userLoggedIn.getId())){
            throw new UserException("No se puede editar un usuario ajeno ", ErrorCodes.USER_DOESNT_EXIST);
        }
        user.setFirstName(userEditDTO.getFirstName());
        user.setLastName(userEditDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userEditDTO.getPassword()));
        userService.save(user);
        return ResponseEntity.ok(user);
    }
}

