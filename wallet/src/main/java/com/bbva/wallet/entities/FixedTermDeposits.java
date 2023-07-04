package com.bbva.wallet.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.Timestamp;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = "fixed_term_deposits")

public class FixedTermDeposits implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private double amount;

    @OneToOne // ó @OneToMany
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @NotNull
    private double interest;

    @CreationTimestamp
    @NotNull
    private LocalDateTime creationDate;

    @Timestamp
    @NotNull
    private LocalDateTime closingDate;
}
