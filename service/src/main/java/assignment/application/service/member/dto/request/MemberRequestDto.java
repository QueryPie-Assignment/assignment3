package assignment.application.service.member.dto.request;

public class MemberRequestDto {
	public record SaveMemberRequestDto(
		String id,
		String password,
		String userName
	) {

	}

	public record LoginMemberRequestDto(
		String id,
		String password
	) {

	}

	public record LogoutMemberRequestDto(
		String id,
		String accessToken,
		String refreshToken
	) {

	}

	public record ReissueTokenRequestDto(
		String id,
		String refreshToken
	) {

	}
}
