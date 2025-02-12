package assignment.application.service.loan.service;

import static assignment.application.service.loan.dto.request.LoanRequestDto.*;
import static assignment.application.service.loan.dto.response.LoanResponseDto.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import assignment.application.domain.book.BookDomain;
import assignment.application.domain.member.MemberDomain;
import assignment.application.infrastructure.book.entity.BookEntity;
import assignment.application.infrastructure.book.repository.BookEntityRepository;
import assignment.application.infrastructure.loan.entity.LoanEntity;
import assignment.application.infrastructure.loan.repository.LoanEntityRepository;
import assignment.application.infrastructure.member.entity.MemberEntity;
import assignment.application.infrastructure.member.repository.MemberEntityRepository;
import assignment.application.service.loan.mapper.LoanServiceMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanService {
	private final LoanEntityRepository loanEntityRepository;
	private final MemberEntityRepository memberEntityRepository;
	private final BookEntityRepository bookEntityRepository;

	private final LoanServiceMapper loanServiceMapper;

	private final MemberDomain memberDomain;
	private final BookDomain bookDomain;

	@Transactional
	public void saveLoan(HttpServletRequest servletRequest, SaveLoanRequestDto requestDto) {
		/*
		 유효성 검사

		 1. Token 유효성 검사
		 2. Member 유효성 검사
		 3. Book 유효성 검사
		 4. 대출 여부 확인
		*/
		String id = memberDomain.extractIdFromRequest(servletRequest);
		MemberEntity memberEntity = memberEntityRepository.findById(id);
		BookEntity bookEntity = bookEntityRepository.findBySeq(requestDto.bookSeq());
		bookDomain.validateIsAvailable(bookEntity.getIsAvailable());

		// isAvailable 갱신
		bookEntity.updateIsAvailable(false);

		loanEntityRepository.save(loanServiceMapper.toLoanEntity(requestDto, memberEntity, bookEntity));
	}

	@Transactional(readOnly = true)
	public CheckLoanResponseDto checkLoan(HttpServletRequest servletRequest, Long bookSeq) {
		/*
		 유효성 검사

		 1. Token 유효성 검사
		 2. Member 유효성 검사
		 3. Book 유효성 검사
		*/
		String id = memberDomain.extractIdFromRequest(servletRequest);
		memberEntityRepository.findById(id);
		BookEntity bookEntity = bookEntityRepository.findBySeq(bookSeq);

		return new CheckLoanResponseDto(bookEntity.getBookSeq(), bookEntity.getIsAvailable());
	}

	@Transactional
	public void deleteLoan(HttpServletRequest servletRequest, DeleteLoanRequestDto requestDto) {
		/*
		 유효성 검사

		 1. Token 유효성 검사
		 2. Member 유효성 검사
		 3. Book 유효성 검사
		 4. 대출 여부 확인
		*/
		String id = memberDomain.extractIdFromRequest(servletRequest);
		MemberEntity memberEntity = memberEntityRepository.findById(id);
		BookEntity bookEntity = bookEntityRepository.findBySeq(requestDto.bookSeq());
		LoanEntity loanEntity = loanEntityRepository.findByMemberEntityAndBookEntity(memberEntity, bookEntity);

		/*
		 1. Loan Entity 삭제
		 2. Book Entity isAvailable 수정
		*/
		loanEntityRepository.delete(loanEntity);

		BookEntity updatedBookEntity = bookEntity.updateIsAvailable(true);
		bookEntityRepository.save(updatedBookEntity);
	}
}
