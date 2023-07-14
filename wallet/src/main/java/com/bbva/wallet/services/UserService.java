package com.bbva.wallet.services;

import com.bbva.wallet.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();
    Page<User> getAllUser(Pageable pageable);
    void softDeleteById(Long id);
    Optional<User> findById(Long id);
    User save(User user);
}
