package com.bbva.wallet.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEditDTO {

    private String firstName;

    private String lastName;

    private String password;
    private String oldPassword;
}
