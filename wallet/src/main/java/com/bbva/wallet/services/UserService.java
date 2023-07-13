package com.bbva.wallet.services;


import com.bbva.wallet.entities.User;
import java.util.List;

public interface UserService {
  
    List<User> getAll();


    void softDeleteById(Long id);

}
