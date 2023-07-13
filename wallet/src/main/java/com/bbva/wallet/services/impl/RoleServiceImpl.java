package com.bbva.wallet.services.impl;

import com.bbva.wallet.entities.Role;
import com.bbva.wallet.enums.RoleName;
import com.bbva.wallet.repositories.RoleRepository;
import com.bbva.wallet.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    public Role getRoleByName(RoleName roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if (role.isEmpty()) {
            Role newRole = Role.builder()
                .name(roleName)
                .build();
            roleRepository.save(newRole);
            return newRole;
        }
        return role.get();
    }
}
