package assignment.presentation.loan.vo.response;

public class LoanResponse {
	public record CheckLoanResponse(
		Long bookSeq,
		Boolean isAvailable
	) {

	}
}
