package assignment.application.service.member.dto.response;

public class MemberResponseDto {
	public record LoginMemberResponseDto(
		String accessToken,
		String refreshToken
	) {

	}

	public record ReissueTokenResponseDto(
		String id,
		String accessToken,
		String refreshToken
	) {

	}

	public record GetMemberResponseDto(
		String id,
		String userName
	) {

	}
}
