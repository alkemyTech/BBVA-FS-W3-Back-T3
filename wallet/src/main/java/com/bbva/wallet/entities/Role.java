package com.bbva.wallet.entities;

import com.bbva.wallet.enums.RoleName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleName name;

    private String description;


    @JsonIgnore
    @CreationTimestamp
    @NotNull
    private LocalDateTime creationDate;

    @JsonIgnore
    @UpdateTimestamp
    @NotNull
    private LocalDateTime updateDate;


}