package com.bbva.wallet.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtAuthResponse {

    String token;
}