package com.bbva.wallet.services;


import com.bbva.wallet.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface UserService {
  
    List<User> getAll();
    Page<User> getAllUser(Pageable pageable);

    void softDeleteById(Long id);

}
