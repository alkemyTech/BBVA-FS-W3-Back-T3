package com.bbva.wallet.seeder;

import com.bbva.wallet.entities.Role;
import com.bbva.wallet.enums.RoleName;
import com.bbva.wallet.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleSeeder {
    private final RoleRepository roleRepository;

    @PostConstruct
    public void seedRoles() {
        if (roleRepository.count() == 0) {
            Role roleAdmin = Role.builder()
                .name(RoleName.ADMIN)
                .description("Administrador")
                .build();
            Role roleUser = Role.builder()
                .name(RoleName.USER)
                .description("Usuario")
                .build();
            roleRepository.saveAll(java.util.List.of(roleAdmin, roleUser));
        }
    }

}
