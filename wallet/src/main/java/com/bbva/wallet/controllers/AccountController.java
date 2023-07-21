package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.AccountDTO;
import com.bbva.wallet.dtos.BalanceDTO;
import com.bbva.wallet.dtos.AccountTransactionLimitDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.exeptions.AccountException;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.TransactionException;
import com.bbva.wallet.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Tag(name = "Accounts")
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    @Operation(summary = "Ver detalle de una cuenta",description = "Ver detalle de una cuenta")
    @GetMapping("/{userId}")
    public ResponseEntity<Iterable<Account>> getUserAccounts(@PathVariable Long userId) {
        Iterable<Account> entities = accountService.getUserAccounts(userId);
        return ResponseEntity.ok(entities);
    }

    @Operation(summary = "Obtener balance",description = "Obtener balance")
    @GetMapping("/balance")
    public ResponseEntity<Optional<BalanceDTO>> getBalance(Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Optional<BalanceDTO> balanceDTO = accountService.getBalance(userLoggedIn.getId());
        return ResponseEntity.ok(balanceDTO);
    }

    @Operation(summary = "Crear una cuenta",description = "Crear una cuenta")
    @SneakyThrows
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountDTO accountDTO, Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Long user_id = userLoggedIn.getId();

        Currency dtoCurrency = accountDTO.getCurrency();

        var userAccounts = accountService.getUserAccounts(user_id);

        if (userAccounts.stream().anyMatch(
                account -> account.getCurrency().equals(dtoCurrency)) ) {
            throw new AccountException("El usuario ya tiene una cuenta en esa moneda", ErrorCodes.ACCOUNT_ALREADY_EXISTS);
        }
        return ResponseEntity.ok(accountService.createAccount(dtoCurrency, userLoggedIn));
    }

    @Operation(summary = "Modificar una cuenta existente",description = "Modificar una cuenta existente")
    @SneakyThrows
    @PatchMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") Long id, @RequestBody AccountTransactionLimitDto accountTransactionLimitDto, Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Account account= accountService.findById(id).orElseThrow(() -> new TransactionException("No existe la cuenta indicada ", ErrorCodes.ACCOUNT_DOESNT_EXIST));

        if (account.getUser().getId()!=userLoggedIn.getId()){
            throw new AccountException("No se puede modificar una cuenta ajena ", ErrorCodes.ACCOUNT_DOESNT_EXIST);
        }

        account.setTransactionLimit(accountTransactionLimitDto.getTransactionLimit());
        accountService.save(account);
        return ResponseEntity.ok(account);

    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Account>>> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<Account> pagedAssembler) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Account> accountPage = accountService.getAllAccounts(pageable);

        PagedModel<EntityModel<Account>> pageModel = pagedAssembler.toModel(accountPage);
        return ResponseEntity.ok(pageModel);
    }
}