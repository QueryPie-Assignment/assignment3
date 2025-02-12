package assignment.application.service.loan.mapper;

import static assignment.application.service.loan.dto.request.LoanRequestDto.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import assignment.application.infrastructure.book.entity.BookEntity;
import assignment.application.infrastructure.loan.entity.LoanEntity;
import assignment.application.infrastructure.member.entity.MemberEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoanServiceMapper {
	@Mapping(target = "loanSeq", ignore = true)
	@Mapping(target = "memberEntity", source = "memberEntity")
	@Mapping(target = "bookEntity", source = "bookEntity")
	LoanEntity toLoanEntity(SaveLoanRequestDto requestDto, MemberEntity memberEntity, BookEntity bookEntity);
}
