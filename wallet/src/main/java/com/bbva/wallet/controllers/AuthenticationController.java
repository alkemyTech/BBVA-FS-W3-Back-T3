package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.JwtAuthResponse;
import com.bbva.wallet.dtos.UserLogInDTO;
import com.bbva.wallet.dtos.UserSignUpDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.exeptions.Response;
import com.bbva.wallet.services.AuthenticationService;
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
import org.springframework.web.bind.annotation.*;

@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Custom Error", content = {
                @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
        }),
        @ApiResponse(responseCode = "403", description = "No autenticado / Token inválido", content = {
                @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
        })
})
@Tag(name = "Authentication")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Registrar un usuario",
            description = "Registrar un usuario",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = JwtAuthResponse.class),
                                            mediaType = "application/json",
                                            examples = @ExampleObject(value =
                                                    "{\n" +
                                                            "  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNTE2MjM5MDIyfQ.t1PFDz1mQO2F2IMg0JUjZAmOIdmyQ4t3m4UjBlf1BqU\",\n" +
                                                            "  \"user\": {\n" +
                                                            "    \"id\": 123,\n" +
                                                            "    \"firstName\": \"John\",\n" +
                                                            "    \"lastName\": \"Doe\",\n" +
                                                            "    \"email\": \"john.doe@example.com\"\n" +
                                                            "  }\n" +
                                                            "}")
                                            )
                            }
                    )
            })
    @PostMapping("/register")
    public ResponseEntity<JwtAuthResponse> singUp (@Valid @RequestBody UserSignUpDTO userDto){
        return ResponseEntity.ok(authenticationService.signUp(userDto));
    }

    @SneakyThrows
    @Operation(
            summary = "Iniciar una sesion",
            description = "Iniciar una sesion",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = JwtAuthResponse.class),
                                            mediaType = "application/json",
                                            examples = @ExampleObject(value =
                                                    "{\n" +
                                                            "  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNTE2MjM5MDIyfQ.t1PFDz1mQO2F2IMg0JUjZAmOIdmyQ4t3m4UjBlf1BqU\",\n" +
                                                            "  \"user\": {\n" +
                                                            "    \"id\": 123,\n" +
                                                            "    \"firstName\": \"John\",\n" +
                                                            "    \"lastName\": \"Doe\",\n" +
                                                            "    \"email\": \"john.doe@example.com\"\n" +
                                                            "  }\n" +
                                                            "}")
                                            )
                            }
                    )
            })
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> LogIn (@Valid @RequestBody UserLogInDTO userDto){
        return ResponseEntity.ok(authenticationService.logIn(userDto));
    }
}

