package assignment.application.infrastructure.loan.repository;

import org.springframework.stereotype.Component;

import assignment.application.infrastructure.book.entity.BookEntity;
import assignment.application.infrastructure.loan.entity.LoanEntity;
import assignment.application.infrastructure.member.entity.MemberEntity;

@Component
public interface LoanEntityRepository {
	LoanEntity save(LoanEntity loanEntity);

	LoanEntity findByMemberEntityAndBookEntity(MemberEntity memberEntity, BookEntity bookEntity);

	void delete(LoanEntity loanEntity);
}
