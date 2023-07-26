package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.LoanRequestDTO;
import com.bbva.wallet.dtos.LoanResponseDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.Response;
import com.bbva.wallet.exeptions.TransactionException;
import com.bbva.wallet.services.LoanService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Loan")
@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Custom Error", content = {
                    @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "403", description = "No autenticado / Token inválido", content = {
                    @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
            })
    })
    @Operation(
            summary = "Crear un prestamo",
            description = "Crear un prestamo",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = LoanResponseDTO.class),
                                            mediaType = "application/json",
                                            examples = @ExampleObject(value =
                                                    "{\n" +
                                                            "  \"monthlyPayment\": 2500,\n" +
                                                            "  \"totalPayment\": 60000,\n" +
                                                            "  \"interestRate\": 5\n" +
                                                            "}")
                                    )
                            }
                    )
            }
    )
    @SneakyThrows
    @PostMapping("/simulate")
    public ResponseEntity<LoanResponseDTO> simulateLoan(@RequestBody @Valid LoanRequestDTO loanRequestDTO) {
   if(loanRequestDTO.getTerm() <= 0){
            throw new TransactionException("La cantidad de meses no debe ser 0 ni debe estar vacio", ErrorCodes.INVALID_AMOUNT);
        }
        double amount = loanRequestDTO.getAmount();
        int term = loanRequestDTO.getTerm();
        LoanResponseDTO loanResponseDTO = loanService.simulate(amount, term);
        return ResponseEntity.ok(loanResponseDTO);
    }
}