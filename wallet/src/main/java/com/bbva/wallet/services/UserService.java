package com.bbva.wallet.services;

import com.bbva.wallet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public void deleteById(Long id) {
        var user = userRepository.findById(id);
        user.ifPresent(value -> {
            value.setSoftDelete(true);
            userRepository.save(value);
        });
    }
}
