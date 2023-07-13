package com.bbva.wallet.services.impl;

import com.bbva.wallet.entities.User;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void softDeleteById(Long id) {
        var user = userRepository.findById(id);
        user.ifPresent(value -> {
            value.setSoftDelete(true);
            userRepository.save(value);
        });
    }

    public Optional<User> findById(Long Id) {
        return userRepository.findById(Id);
    }

    public User save(User user){return userRepository.save(user);}


}