package assignment.application.exception.message;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorResult {

	// 400 BAD REQUEST
	DTO_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"요청 데이터 형식 오류",
		"B001"
	),

	ID_PASSWORD_MISMATCH_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"아이디 비밀번호가 틀렸습니다.",
		"IPMB002"
	),

	ALREADY_LOAN_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"이미 대출 받은 책 입니다.",
		"ALB003"
	),

	// 401 UnAuthorized Exception
	TOKEN_UNAUTHORIZED_EXCEPTION(
		HttpStatus.UNAUTHORIZED.value(),
		"올바르지 않은 토큰입니다.",
		"TU001"
	),

	// 404 NOT FOUND
	ID_NOT_FOUND_EXCEPTION(
		HttpStatus.NOT_FOUND.value(),
		"존재하지 않는 아이디입니다.",
		"INF001"
	),

	BOOK_SEQ_NOT_FOUND_EXCEPTION(
		HttpStatus.NOT_FOUND.value(),
		"존재하지 않는 Book Seq입니다.",
		"BSN002"
	),

	BOOK_TAG_NOT_FOUND_EXCEPTION(
		HttpStatus.NOT_FOUND.value(),
		"존재하지 않는 Book Tag입니다.",
		"BTN003"
	),

	LOAN_BOOK_NOT_FOUND_EXCEPTION(
		HttpStatus.NOT_FOUND.value(),
		"해당 회원이 빌린 책이 없습니다.",
		"LBN004"
	),


	// 409 CONFLICT ERROR
	ID_DUPLICATION_CONFLICT_EXCEPTION(
		HttpStatus.CONFLICT.value(),
		"중복된 ID 값 입니다.",
		"IDC001"
	),

	// 500 SERVER ERROR
	UNKNOWN_EXCEPTION(
		HttpStatus.INTERNAL_SERVER_ERROR.value(),
		"서버 오류",
		"S500"
	),

	;

	private final int httpStatus;
	private final String message;
	private final String code;
}
