package assignment.application.infrastructure.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import assignment.application.infrastructure.loan.entity.LoanEntity;

@Repository
public interface LoanEntityJpaRepository extends JpaRepository<LoanEntity, Long> {

}
