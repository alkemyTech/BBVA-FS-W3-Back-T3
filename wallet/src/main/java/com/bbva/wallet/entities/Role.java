package com.bbva.wallet.entities;

import com.bbva.wallet.enums.RoleName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(unique = true)
    private RoleName name;

    private String description;


    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime creationDate;

    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime updateDate;


}