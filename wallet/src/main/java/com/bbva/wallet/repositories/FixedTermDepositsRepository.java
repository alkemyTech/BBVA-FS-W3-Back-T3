package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.FixedTermDeposits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedTermDepositsRepository extends JpaRepository<FixedTermDeposits, Long> {
}
