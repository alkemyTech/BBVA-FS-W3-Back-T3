package com.bbva.wallet.entities;

import com.bbva.wallet.enums.Currency;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Where(clause = "soft_delete = false")
@Table(name = "accounts")
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull
    private Double transactionLimit;

    @NotNull
    private Double balance;

    @NotNull
    @Column(unique = true)
    private String cbu;

    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime creationDate;

    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @JsonIgnore
    private boolean softDelete;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
