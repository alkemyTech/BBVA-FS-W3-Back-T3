package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.FixedTermDepositsDTO;
import com.bbva.wallet.dtos.FixedTermDepositsSimulationDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposits;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.exeptions.AccountException;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.Response;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.FixedTermDepositsService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Custom Error", content = {
                @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
        }),
        @ApiResponse(responseCode = "403", description = "No autenticado / Token inválido", content = {
                @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
        })
})
@Tag(name = "FixedTermDeposits")
@RestController
@RequestMapping("/fixedTerm")
@RequiredArgsConstructor
public class FixedTermDepositsController {
    private final FixedTermDepositsService fixedTermDepositsService;
    private final AccountService accountService;
    @Value("${fixedTermDeposit.currency}")
    private Currency currency;

    @Operation(
            summary = "Crear un deposito a plazo fijo",
            description = "Crear un deposito a plazo fijo",
            responses ={
            @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = FixedTermDeposits.class),
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value =
                                            "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"amount\": 1000,\n" +
                                                    "  \"account\": {\n" +
                                                    "    \"id\": 1,\n" +
                                                    "    \"currency\": \"ARS\",\n" +
                                                    "    \"transactionLimit\": 5000,\n" +
                                                    "    \"balance\": 3000,\n" +
                                                    "    \"cbu\": \"0123456789012345678901\"\n" +
                                                    "  },\n" +
                                                    "  \"interest\": 5,\n" +
                                                    "  \"creationDate\": \"2023-07-26T14:42:28.789Z\",\n" +
                                                    "  \"closingDate\": \"2023-08-26T14:42:28.789Z\"\n" +
                                                    "}"))
                    }
            )
    })
    @SneakyThrows
    @PostMapping
    public ResponseEntity<FixedTermDeposits> createFixedTermDeposit(@Valid @RequestBody FixedTermDepositsDTO fixedTermDepositsDTO,
    Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();

        Account account =
                accountService.getAccountByUserIdAndCurrency(userLoggedIn.getId(), currency).orElseThrow(
                        () -> new AccountException("El usuario no tiene account en esta moneda: " + currency,
                                ErrorCodes.ACCOUNT_DOESNT_EXIST));

        FixedTermDeposits fixedTermDeposits = fixedTermDepositsService.createFixedTermDeposit(
                fixedTermDepositsDTO,
                account);

        accountService.save(account);

        return ResponseEntity.ok().body(fixedTermDeposits);
    }

    @Operation(
            summary = "Crear una simulacion de deposito",
            description = "Crear una simulacion de deposito",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = FixedTermDepositsSimulationDTO.class),
                                            mediaType = "application/json",
                                            examples = @ExampleObject(value =
                                            "{\n" +
                                                    "  \"amount\": 1000,\n" +
                                                    "  \"interest\": 5,\n" +
                                                    "  \"total\": 1050,\n" +
                                                    "  \"closingDate\": \"2023-07-26T14:44:05.966Z\",\n" +
                                                    "  \"creationDate\": \"2023-07-26T14:44:05.966Z\"\n" +
                                                    "}"))
                            }
                    )
            })
    @SneakyThrows
    @PostMapping("/simulate")
    public ResponseEntity<FixedTermDepositsSimulationDTO> simulateFixedTermDeposit(@Valid @RequestBody FixedTermDepositsDTO fixedTermDepositsDTO) {
        FixedTermDepositsSimulationDTO simulation = fixedTermDepositsService.createFixedTermDeposit(fixedTermDepositsDTO);
        return ResponseEntity.ok().body(simulation);
    }
}
