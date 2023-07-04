package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
