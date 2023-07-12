package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.enums.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUserIdAndCurrency(Long id, Currency currency);
  List<Account> findByUserId(Long id);
}

