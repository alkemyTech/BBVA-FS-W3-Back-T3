package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedTermDepositsRepository extends JpaRepository<FixedTermDeposits, Long> {
    List<FixedTermDeposits> findAllByAccount(Account arsAccount);
}
