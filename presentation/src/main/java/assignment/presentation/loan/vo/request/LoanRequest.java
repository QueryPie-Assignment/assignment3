package assignment.presentation.loan.vo.request;

import static assignment.application.service.loan.dto.request.LoanRequestDto.*;

import java.time.LocalDate;

import assignment.application.exception.message.ErrorResult;
import assignment.application.exception.status.BadRequestException;

public class LoanRequest {
	public record SaveLoanRequest(
		Long bookSeq,
		LocalDate loanDate,
		LocalDate dueDate,
		LocalDate returnDate

	) {
		public void validateSaveLoanRequest() {
			if (this.bookSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.loanDate == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.dueDate == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.returnDate == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}

		public SaveLoanRequestDto toSaveLoanRequestDto() {
			return new SaveLoanRequestDto(
				this.bookSeq,
				this.loanDate,
				this.dueDate,
				this.returnDate
			);
		}
	}

	public record DeleteLoanRequest(
		Long bookSeq
	) {
		public void validateDeleteLoanRequest() {
			if (this.bookSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}

		public DeleteLoanRequestDto toDeleteLoanRequestDto() {
			return new DeleteLoanRequestDto(
				this.bookSeq
			);
		}
	}
}
