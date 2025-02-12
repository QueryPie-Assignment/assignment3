package assignment.application.service.loan.dto.request;

import java.time.LocalDate;

public class LoanRequestDto {
	public record SaveLoanRequestDto(
		Long bookSeq,
		LocalDate loanDate,
		LocalDate dueDate,
		LocalDate returnDate
	) {

	}

	public record DeleteLoanRequestDto(
		Long bookSeq
	) {

	}
}
