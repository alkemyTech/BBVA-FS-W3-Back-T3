package com.bbva.wallet.controllers;


import com.bbva.wallet.dtos.BalanceDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    @GetMapping("/{userId}")
    public ResponseEntity<Iterable<Account>> getUserAccounts(@PathVariable Long userId) {
       Iterable<Account> entities =accountService.getUserAccounts(userId);
       return ResponseEntity.ok(entities);
   }

    @GetMapping("/balance")
    public ResponseEntity<Optional<BalanceDTO>> getBalance(Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Optional<BalanceDTO> balanceDTO = accountService.getBalance(userLoggedIn.getId());
        return ResponseEntity.ok(balanceDTO);
    }


    }
}