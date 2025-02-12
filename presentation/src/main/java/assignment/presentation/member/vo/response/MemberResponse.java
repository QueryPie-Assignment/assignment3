package assignment.presentation.member.vo.response;

public class MemberResponse {
	public record LoginMemberResponse(
		String accessToken,
		String refreshToken
	) {

	}

	public record ReissueTokenResponse(
		String id,
		String accessToken,
		String refreshToken
	) {

	}

	public record GetMemberResponse (
		String id,
		String userName
	) {

	}
}
