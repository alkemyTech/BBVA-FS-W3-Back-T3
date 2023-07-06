package com.bbva.wallet.services.impl;

import com.bbva.wallet.entities.User;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;


    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
