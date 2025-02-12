package assignment.application.infrastructure.loan.repository;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import assignment.application.exception.message.ErrorResult;
import assignment.application.exception.status.NotFoundRequestException;
import assignment.application.infrastructure.book.entity.BookEntity;
import assignment.application.infrastructure.loan.entity.LoanEntity;
import assignment.application.infrastructure.loan.entity.QLoanEntity;
import assignment.application.infrastructure.member.entity.MemberEntity;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoanEntityRepositoryImpl implements LoanEntityRepository {
	private final LoanEntityJpaRepository loanEntityJpaRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public LoanEntity save(LoanEntity loanEntity) {
		return loanEntityJpaRepository.save(loanEntity);
	}

	@Override
	public LoanEntity findByMemberEntityAndBookEntity(MemberEntity memberEntity, BookEntity bookEntity) {
		QLoanEntity qLoanEntity = QLoanEntity.loanEntity;

		return Optional.ofNullable(
			queryFactory
				.selectFrom(qLoanEntity)
				.where(
					qLoanEntity.memberEntity.eq(memberEntity)
						.and(qLoanEntity.bookEntity.eq(bookEntity))
				).fetchOne()
		).orElseThrow(() -> new NotFoundRequestException(ErrorResult.LOAN_BOOK_NOT_FOUND_EXCEPTION));
	}

	@Override
	public void delete(LoanEntity loanEntity) {
		loanEntityJpaRepository.delete(loanEntity);
	}
}
