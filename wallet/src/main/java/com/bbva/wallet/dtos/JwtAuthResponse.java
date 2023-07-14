package com.bbva.wallet.dtos;


import com.bbva.wallet.entities.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtAuthResponse {

    String token;
    User user;
}