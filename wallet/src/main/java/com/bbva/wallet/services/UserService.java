package com.bbva.wallet.services;

import com.bbva.wallet.entities.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll();

    void softDeleteById(Long id);

    Optional<User> findById(Long id);
}
