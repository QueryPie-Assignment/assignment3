package assignment.application.service.loan.dto.response;

public class LoanResponseDto {
	public record CheckLoanResponseDto(
		Long bookSeq,
		Boolean isAvailable
	) {

	}
}
