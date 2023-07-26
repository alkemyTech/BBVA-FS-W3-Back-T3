package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.UserEditDTO;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.Response;
import com.bbva.wallet.exeptions.TransactionException;
import com.bbva.wallet.exeptions.UserException;
import com.bbva.wallet.services.UserService;
import com.bbva.wallet.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Objects;

@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Custom Error", content = {
                @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
        }),
        @ApiResponse(responseCode = "403", description = "No autenticado / Token inválido", content = {
                @Content(schema = @Schema(implementation = Response.class), mediaType = "application/json")
        })
})
@Tag(name = "Users")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @Operation(
            summary = "Eliminar un usuario",
            description = "Eliminar un usuario",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = User.class),
                                            mediaType = "application/json",
                                            examples = @ExampleObject(value =
                                                            "  \"id\": 12345,\n" +
                                                            "  \"firstName\": \"John\",\n" +
                                                            "  \"lastName\": \"Doe\",\n" +
                                                            "  \"email\": \"john.doe@example.com\"\n" +
                                                            "}"))
                            }
                    )
            })
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.getId ")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id ) {
        userService.softDeleteById(id);
        accountService.softDeleteByUserId(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Listar todos los usuarios",
            description = "Listar todos los usuarios",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = User.class),
                                            mediaType = "application/json",
                                            examples = @ExampleObject(value =
                                                    "  \"id\": 12345,\n" +
                                                    "  \"firstName\": \"John\",\n" +
                                                    "  \"lastName\": \"Doe\",\n" +
                                                    "  \"email\": \"john.doe@example.com\"\n" +
                                                    "}"))
                            }
                    )
            })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<User>>> getAllUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<User> pagedAssembler) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<User> userPage = userService.getAllUser(pageable);

        PagedModel<EntityModel<User>> pageModel = pagedAssembler.toModel(userPage);
        return ResponseEntity.ok(pageModel);
    }

    @Operation(
            summary = "Obtener usuario",
            description = "Obtener usuario",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(
                                            implementation = User.class),
                                            mediaType = "application/json",
                                            examples = @ExampleObject(value =
                                            "{\n" +
                                                    "  \"id\": 12345,\n" +
                                                    "  \"firstName\": \"John\",\n" +
                                                    "  \"lastName\": \"Doe\",\n" +
                                                    "  \"email\": \"john.doe@example.com\"\n" +
                                                    "}"))
                            }
                    )
            })
    @SneakyThrows
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.getId")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        User user = userService.findById(id).orElseThrow(() -> new TransactionException("No existe el usuario " +
                "indicado ", ErrorCodes.USER_DOESNT_EXIST));

        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Editar un usuario",
            description = "Editar un usuario",
            responses ={
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = User.class), mediaType = "application/json",examples = @ExampleObject(value =
                                            "{\n" +
                                                    "  \"id\": 12345,\n" +
                                                    "  \"firstName\": \"John\",\n" +
                                                    "  \"lastName\": \"Doe\",\n" +
                                                    "  \"email\": \"john.doe@example.com\"\n" +
                                                    "}"))
                            }
                    )
            })
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