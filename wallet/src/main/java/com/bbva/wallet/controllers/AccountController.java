package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.CreateAccountDTO;
import com.bbva.wallet.dtos.BalanceResponseDTO;
import com.bbva.wallet.dtos.AccountTransactionLimitDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.exeptions.AccountException;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.Response;
import com.bbva.wallet.exeptions.TransactionException;
import com.bbva.wallet.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import java.util.Objects;

@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Custom Error", content = {
                @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
        }),
        @ApiResponse(responseCode = "403", description = "No autenticado / Token inválido", content = {
                @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
        })
})
@Tag(name = "Accounts")
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @Operation(
            summary = "Ver detalle de una cuenta",
            description = "Ver detalle de una cuenta",

    responses ={
        @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = {
                        @Content(mediaType = "application/json",
                                schema = @Schema(implementation = Account.class),
                                examples = @ExampleObject(value =
                                        "{\n" +
                                                "  \"id\": 1,\n" +
                                                "  \"currency\": \"ARS\",\n" +
                                                "  \"transactionLimit\": 5000,\n" +
                                                "  \"balance\": 25000,\n" +
                                                "  \"cbu\": \"0123456789012345678901\"\n" +
                                                "}")
                        )
                }
        )
    })
    @PreAuthorize("hasAuthority('ADMIN') or #userId == authentication.principal.getId ")
    @GetMapping("/{userId}")
    public ResponseEntity<Iterable<Account>> getUserAccounts(@PathVariable Long userId) {
        Iterable<Account> entities = accountService.getUserAccounts(userId);
        return ResponseEntity.ok(entities);
    }

    @Operation(
            summary = "Obtener balance",
            description = "Obtener balance",
            responses ={
            @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(
                                    implementation = BalanceResponseDTO.class),
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value =
                                            "{\n" +
                                                    "  \"accountArs\": 1000,\n" +
                                                    "  \"accountUsd\": 500,\n" +
                                                    "  \"history\": [\n" +
                                                    "    {\n" +
                                                    "      \"id\": 1,\n" +
                                                    "      \"amount\": 200,\n" +
                                                    "      \"type\": \"INCOME\",\n" +
                                                    "      \"description\": \"Income from sale\",\n" +
                                                    "      \"account\": {\n" +
                                                    "        \"id\": 1,\n" +
                                                    "        \"currency\": \"ARS\",\n" +
                                                    "        \"transactionLimit\": 5000,\n" +
                                                    "        \"balance\": 3000,\n" +
                                                    "        \"cbu\": \"0123456789012345678901\"\n" +
                                                    "      }\n" +
                                                    "    }\n" +
                                                    "  ],\n" +
                                                    "  \"fixedTerms\": [\n" +
                                                    "    {\n" +
                                                    "      \"id\": 1,\n" +
                                                    "      \"amount\": 1000,\n" +
                                                    "      \"account\": {\n" +
                                                    "        \"id\": 2,\n" +
                                                    "        \"currency\": \"ARS\",\n" +
                                                    "        \"transactionLimit\": 5000,\n" +
                                                    "        \"balance\": 5000,\n" +
                                                    "        \"cbu\": \"0987654321098765432109\"\n" +
                                                    "      },\n" +
                                                    "      \"interest\": 5,\n" +
                                                    "      \"creationDate\": \"2023-07-26T14:27:23.765Z\",\n" +
                                                    "      \"closingDate\": \"2023-08-26T14:27:23.765Z\"\n" +
                                                    "    }\n" +
                                                    "  ]\n" +
                                                    "}"))




                    }
            )
    }
    )
    @GetMapping("/balance")
    public ResponseEntity<BalanceResponseDTO> getBalance(Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        BalanceResponseDTO balanceResponseDTO = accountService.getBalance(userLoggedIn.getId());
        return ResponseEntity.ok(balanceResponseDTO);
    }

    @Operation(summary = "Crear una cuenta",
            description = "Crear una cuenta",
            responses ={
            @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = Account.class),
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value =
                                    "{\n" +
                                            "  \"id\": 123,\n" +
                                            "  \"currency\": \"ARS\",\n" +
                                            "  \"transactionLimit\": 10000,\n" +
                                            "  \"balance\": 50000,\n" +
                                            "  \"cbu\": \"0123456789012345678901\"\n" +
                                            "}"))
                    }
            )
    })
    @SneakyThrows
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountDTO createAccountDTO, Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Long user_id = userLoggedIn.getId();

        Currency dtoCurrency = createAccountDTO.getCurrency();

        var userAccounts = accountService.getUserAccounts(user_id);

        if (userAccounts.stream().anyMatch(
                account -> account.getCurrency().equals(dtoCurrency)) ) {
            throw new AccountException("El usuario ya tiene una cuenta en esa moneda", ErrorCodes.ACCOUNT_ALREADY_EXISTS);
        }
        return ResponseEntity.ok(accountService.createAccount(dtoCurrency, userLoggedIn));
    }

    @Operation(
            summary = "Modificar una cuenta existente",
            description = "Modificar una cuenta existente",
            responses ={
            @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = Account.class),
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value =
                                    "{\n" +
                                            "  \"transactionLimit\": 10000\n" +
                                            "}"))
                    }
            )
    })
    @SneakyThrows
    @PatchMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") Long id,
                                                 @Valid @RequestBody AccountTransactionLimitDto accountTransactionLimitDto,
                                                 Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Account account= accountService.findById(id).orElseThrow(() -> new TransactionException("No existe la cuenta indicada ", ErrorCodes.ACCOUNT_DOESNT_EXIST));

        if (!Objects.equals(account.getUser().getId(), userLoggedIn.getId())){
            throw new AccountException("No se puede modificar una cuenta ajena ", ErrorCodes.ACCOUNT_DOESNT_EXIST);
        }

        account.setTransactionLimit(accountTransactionLimitDto.getTransactionLimit());
        accountService.save(account);
        return ResponseEntity.ok(account);

    }
    @Operation(
            summary = "Obtener cuentas",
            description = "Obtener cuentas",responses ={
            @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = Account.class),
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value =
                                            "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"currency\": \"ARS\",\n" +
                                                    "  \"transactionLimit\": 1000,\n" +
                                                    "  \"balance\": 5000,\n" +
                                                    "  \"cbu\": \"0123456789012345678901\"\n" +
                                                    "}"))
                    }
            )
    })
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