package com.bbva.wallet.dtos;

import lombok.*;



@Builder

public record JwtAuthResponse(String token) {}