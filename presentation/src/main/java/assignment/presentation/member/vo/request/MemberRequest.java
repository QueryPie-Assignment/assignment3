package assignment.presentation.member.vo.request;

import static assignment.application.service.member.dto.request.MemberRequestDto.*;

import assignment.application.exception.message.ErrorResult;
import assignment.application.exception.status.BadRequestException;

public class MemberRequest {
	public record SaveMemberRequest(
		String id,
		String userName,
		String password
	) {
		public void validateSaveMemberRequest() {
			if (this.id == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.userName == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.password == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}

		public SaveMemberRequestDto toSaveMemberRequestDto() {
			return new SaveMemberRequestDto(
				this.id,
				this.userName,
				this.password
			);
		}
	}

	public record LoginMemberRequest(
		String id,
		String password
	) {
		public void validateLoginMemberRequest() {
			if (this.id == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.password == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}

		public LoginMemberRequestDto toLoginMemberRequestDto() {
			return new LoginMemberRequestDto(
				this.id,
				this.password
			);
		}
	}
}
