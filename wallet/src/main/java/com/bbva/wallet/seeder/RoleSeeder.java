package com.bbva.wallet.seeder;

import com.bbva.wallet.entities.Role;
import com.bbva.wallet.enums.RoleName;
import com.bbva.wallet.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleSeeder {
    private final RoleRepository roleRepository;

    public List<Role> seedRoles() {
        if (roleRepository.count() != 0) {
            return roleRepository.findAll();
        }
        Role roleAdmin = Role.builder()
                .name(RoleName.ADMIN)
                .description("Administrador")
                .build();
        Role roleUser = Role.builder()
                .name(RoleName.USER)
                .description("Usuario")
                .build();
        return roleRepository.saveAll(java.util.List.of(roleAdmin, roleUser));
    }

}
