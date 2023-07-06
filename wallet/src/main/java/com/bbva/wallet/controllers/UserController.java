package com.bbva.wallet.controllers;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/accounts/{userId}")
    public List<Account> getUserAccounts(@PathVariable Long userId) {
        return userService.getUserAccounts(userId);
    }
}