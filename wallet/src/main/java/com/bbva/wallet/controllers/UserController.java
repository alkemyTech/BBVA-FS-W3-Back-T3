package com.bbva.wallet.controllers;

import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.RoleName;
import com.bbva.wallet.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

//    @PreAuthorize("hasRole('ADMIN')") todo: revisar, no filtra por rol ni por id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id, Authentication authentication ) {
        User userLoggedIn = (User) authentication.getPrincipal();
        if( userLoggedIn.getId() != id && !(userLoggedIn.getAuthorities().contains(RoleName.ADMIN) )) {
            return ResponseEntity.status(403).body("No tienes permisos para eliminar este usuario");
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
