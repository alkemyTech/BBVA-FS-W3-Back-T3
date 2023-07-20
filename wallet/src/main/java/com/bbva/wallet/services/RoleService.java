package com.bbva.wallet.services;

import com.bbva.wallet.entities.Role;
import com.bbva.wallet.enums.RoleName;

public interface RoleService {
    Role getRoleByName(RoleName roleName);
}
