package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.*;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.enums.RoleName;
import com.bbva.wallet.exeptions.AccountException;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.Response;
import com.bbva.wallet.exeptions.TransactionException;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.bbva.wallet.utils.Utils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Custom Error", content = {
                @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
        }),
        @ApiResponse(responseCode = "403", description = "No autenticado / Token inválido", content = {
                @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
        })
})
@Tag(name = "Transactions")
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final AccountService accountService;
    private final Utils utils;

    @Operation(
            summary = "Crear una transaccion en pesos",
            description = "Crear una transaccion en pesos",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "201",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = Transaction.class),
                                            mediaType = "application/json",
                                            examples = @ExampleObject(value =
                                                    "{\n" +
                                                            "  \"id\": 1,\n" +
                                                            "  \"amount\": 500,\n" +
                                                            "  \"type\": \"INCOME\",\n" +
                                                            "  \"description\": \"Salary\",\n" +
                                                            "  \"account\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"currency\": \"ARS\",\n" +
                                                            "    \"transactionLimit\": 5000,\n" +
                                                            "    \"balance\": 3000,\n" +
                                                            "    \"cbu\": \"0123456789012345678901\"\n" +
                                                            "  }\n" +
                                                            "}"))
                            }
                    )
            })
    @PostMapping("/sendArs")
    public ResponseEntity<Transaction> sendArs(@RequestBody @Valid TransactionRequestDTO transactionDto
            , Authentication authentication) {
        return send(transactionDto, Currency.ARS, authentication);
    }

    @Operation(
            summary = "Crear una transaccion en dolares",
            description = "Crear una transaccion en dolares",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "201",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = Response.class),
                                            mediaType = "application/json",
                                            examples = @ExampleObject(value =
                                                    "{\n" +
                                                            "  \"id\": 1,\n" +
                                                            "  \"amount\": 500,\n" +
                                                            "  \"type\": \"INCOME\",\n" +
                                                            "  \"description\": \"Salary\",\n" +
                                                            "  \"account\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"currency\": \"ARS\",\n" +
                                                            "    \"transactionLimit\": 5000,\n" +
                                                            "    \"balance\": 3000,\n" +
                                                            "    \"cbu\": \"0123456789012345678901\"\n" +
                                                            "  }\n" +
                                                            "}"))
                            }
                    )

            })
    @PostMapping("/sendUsd")
    public ResponseEntity<Transaction> sendUsd(@RequestBody @Valid TransactionRequestDTO transactionDto, Authentication authentication) {
        return send(transactionDto, Currency.USD, authentication);
    }

    @SneakyThrows
    private ResponseEntity<Transaction> send(@RequestBody TransactionRequestDTO transactionDto, Currency currency, Authentication authentication) {
        Account sourceAccount = getUserLoggedInAccount(authentication, currency);
        Account destinationAccount = accountService.findById(transactionDto.getDestinationAccountId()).orElseThrow(() -> new AccountException("La cuenta destino no existe ", ErrorCodes.ACCOUNT_DOESNT_EXIST));

        Transaction payment = transactionService.send(transactionDto, sourceAccount, destinationAccount);
        accountService.saveAll(List.of(sourceAccount, destinationAccount));
        return ResponseEntity.ok(payment);
    }

    //------------------------------------------Deposit--------------------------------------------------------------
    @Operation(
            summary = "Crear un deposito",
            description = "Crear un deposito",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = Transaction.class),
                                            mediaType = "application/json",
                                            examples = @ExampleObject(value =
                                                    "{\n" +
                                                            "  \"id\": 1,\n" +
                                                            "  \"amount\": 500,\n" +
                                                            "  \"type\": \"INCOME\",\n" +
                                                            "  \"description\": \"Salary\",\n" +
                                                            "  \"account\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"currency\": \"ARS\",\n" +
                                                            "    \"transactionLimit\": 5000,\n" +
                                                            "    \"balance\": 3000,\n" +
                                                            "    \"cbu\": \"0123456789012345678901\"\n" +
                                                            "  }\n" +
                                                            "}")
                                    )

                            }
                    )
            })
    @SneakyThrows
    @PostMapping("/deposit")
    public ResponseEntity<TransactionCreatedResponse> deposit(@Valid @Parameter (
            example = "{\n" +
                    "  \"destinationAccountId\": 124,\n" +
                    "  \"amount\": 1000,\n" +
                    "  \"transactionDate\": \"2023-07-26T15:31:54.800Z\",\n" +
                    "  \"description\": \"Pago de factura\"\n" +
                    "}")
                                                                  @RequestBody DepositRequestDTO depositDTO, Authentication authentication) {
        Account sourceAccount = getUserLoggedInAccount(authentication, depositDTO.getCurrency());

        TransactionCreatedResponse depositCreatedResponse = transactionService.deposit(sourceAccount, depositDTO.getAmount());
        return ResponseEntity.ok(depositCreatedResponse);
    }
    //------------------------------------------Payment--------------------------------------------------------------
    @Operation(
            summary = "Crear un pago",
            description = "Crear un pago",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "201",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = TransactionCreatedResponse.class),
                                            mediaType = "application/json",
                                            examples = @ExampleObject(value =
                                                    "{\n" +
                                                            "  \"amount\": 5000,\n" +
                                                            "  \"currency\": \"ARS\",\n" +
                                                            "  \"balance\": 10000,\n" +
                                                            "  \"accountId\": 12345\n" +
                                                            "}"))

                            }
                    )
            })
    @SneakyThrows
    @PostMapping("/payment")
    public ResponseEntity<TransactionCreatedResponse> payment(@Valid @Parameter (
            example = "{\n" +
                    "  \"amount\": 200,\n" +
                    "  \"currency\": \"ARS\"\n" +
                    "}")
                                                                  @RequestBody PaymentRequestDTO paymentDTO, Authentication authentication) {
        Account sourceAccount = getUserLoggedInAccount(authentication, paymentDTO.getCurrency());

        TransactionCreatedResponse paymentCreatedDTO = transactionService.payment(sourceAccount, paymentDTO.getAmount());
        return ResponseEntity.ok(paymentCreatedDTO);
    }
    private Account getUserLoggedInAccount(Authentication authentication, Currency dtoCurrency) throws AccountException {
        User userLoggedIn = (User) authentication.getPrincipal();
        return accountService.findByUserIdAndCurrency(userLoggedIn.getId(), dtoCurrency)
                .orElseThrow(() -> new AccountException("El usuario no posee una cuenta en " + dtoCurrency, ErrorCodes.ACCOUNT_DOESNT_EXIST));
    }


    @Operation(
            summary = "Ver detalle de una transaccion",
            description = "Ver detalle de una transaccion",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = Transaction.class),
                                            mediaType = "application/json",
                                            examples = @ExampleObject(value =
                                                    "{\n" +
                                                            "  \"id\": 1,\n" +
                                                            "  \"amount\": 500,\n" +
                                                            "  \"type\": \"INCOME\",\n" +
                                                            "  \"description\": \"Salary\",\n" +
                                                            "  \"account\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"currency\": \"ARS\",\n" +
                                                            "    \"transactionLimit\": 5000,\n" +
                                                            "    \"balance\": 3000,\n" +
                                                            "    \"cbu\": \"0123456789012345678901\"\n" +
                                                            "  }\n" +
                                                            "}"))
                            }
                    )
            })
    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> transactionsDetails(@PathVariable("id") Long id, Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Transaction transaction = transactionService.findById(id).orElseThrow(() -> new TransactionException("La transacción no existe", ErrorCodes.TRANSACTION_DOESNT_EXIST));
        Account sourceAccount = accountService.findById(transaction.getAccount().getId()).orElseThrow(() -> new AccountException("No existe la cuenta", ErrorCodes.ACCOUNT_DOESNT_EXIST));

        if(sourceAccount.getUser().getId().equals(userLoggedIn.getId()) || userLoggedIn.getRole().getName().equals(RoleName.ADMIN)){
            return ResponseEntity.ok(transaction);
        } else {
            throw new TransactionException("No tiene permisos para acceder a esta transacción", ErrorCodes.TRANSACTION_DOESNT_EXIST);
        }


}
    @Operation(
            summary = "Modificar una transaccion",
            description = "Modificar una transaccion",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = Transaction.class),
                                            mediaType = "application/json",
                                            examples = @ExampleObject(value =
                                            "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"amount\": 500,\n" +
                                                    "  \"type\": \"INCOME\",\n" +
                                                    "  \"description\": \"Salary\",\n" +
                                                    "  \"account\": {\n" +
                                                    "    \"id\": 1,\n" +
                                                    "    \"currency\": \"ARS\",\n" +
                                                    "    \"transactionLimit\": 5000,\n" +
                                                    "    \"balance\": 3000,\n" +
                                                    "    \"cbu\": \"0123456789012345678901\"\n" +
                                                    "  }\n" +
                                                    "}"))
                            }
                    )
            })
    @SneakyThrows
    @PatchMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable("id") Long id,
                                                         @Parameter (
                                                                 example = "{\n" +
                                                                         "  \"description\": \"Compra de propiedad\"\n" +
                                                                         "}")
                                                         @Valid @RequestBody TransactionDescriptionDto transactionDescriptionDto,
                                                         Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Transaction transaction= transactionService.findById(id).orElseThrow(() -> new TransactionException("No existe la transaccion indicada ", ErrorCodes.TRANSACTION_DOESNT_EXIST));
        Account sourceAccount = accountService.findById(transaction.getAccount().getId()).orElseThrow(() -> new AccountException("No existe la cuenta ", ErrorCodes.ACCOUNT_DOESNT_EXIST));

        if (!Objects.equals(sourceAccount.getUser().getId(), userLoggedIn.getId())){
           throw new TransactionException("No se puede modificar una transaccion ajena ", ErrorCodes.TRANSACTION_DOESNT_EXIST);
        }

        transaction.setDescription(transactionDescriptionDto.getDescription());
        transactionService.save(transaction);

       return ResponseEntity.ok(transaction);

    }

    @Operation(
            summary = "Listar todas las transacciones del usuario",
            description = "Listar todas las transacciones del usuario",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = Transaction.class),
                                            mediaType = "application/json",
                                            examples = @ExampleObject(value =
                                                    "{\n" +
                                                            "  \"id\": 1,\n" +
                                                            "  \"amount\": 500,\n" +
                                                            "  \"type\": \"INCOME\",\n" +
                                                            "  \"description\": \"Salary\",\n" +
                                                            "  \"account\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"currency\": \"ARS\",\n" +
                                                            "    \"transactionLimit\": 5000,\n" +
                                                            "    \"balance\": 3000,\n" +
                                                            "    \"cbu\": \"0123456789012345678901\"\n" +
                                                            "  }\n" +
                                                            "}"))

                            }
                    )
            })
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.getId ")
    @GetMapping("/userId/{id}")
    public ResponseEntity<PagedModel<EntityModel<Transaction>>> getUserTransactions(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<Transaction> pagedAssembler) {

        List<Account> accounts = accountService.findByUserId(id);
        Iterable<Transaction> transactions = transactionService.getUserTransaction(accounts);

        Page<Transaction> transactionPage = utils.paginateTransactions(transactions, page, size);

        PagedModel<EntityModel<Transaction>> pagedModel = pagedAssembler.toModel(transactionPage);
        return ResponseEntity.ok(pagedModel);
    }
}