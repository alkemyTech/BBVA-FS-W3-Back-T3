package com.bbva.wallet.repositories;


import com.bbva.wallet.entities.Role;
import com.bbva.wallet.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository <Role, Long  > {
    Optional<Role> findByName(RoleName roleName);
}
