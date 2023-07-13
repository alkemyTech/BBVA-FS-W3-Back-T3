package com.bbva.wallet.services.impl;

import com.bbva.wallet.entities.User;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> getAllUser(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public void softDeleteById(Long id) {
        var user = userRepository.findById(id);
        user.ifPresent(value -> {
            value.setSoftDelete(true);
            userRepository.save(value);
        });
    }
}

