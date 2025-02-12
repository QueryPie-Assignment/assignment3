package assignment.presentation.member.controller;

import static assignment.application.service.member.dto.response.MemberResponseDto.*;
import static assignment.presentation.member.vo.request.MemberRequest.*;
import static assignment.presentation.member.vo.response.MemberResponse.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import assignment.application.exception.GlobalExceptionHandler;
import assignment.application.service.member.service.MemberService;
import assignment.presentation.base.BaseResponse;
import assignment.presentation.member.mapper.MemberControllerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "회원")
public class MemberController {
	private final MemberService memberService;

	private final MemberControllerMapper memberControllerMapper;

	@PostMapping()
	@Operation(summary = "회원가입 API", description = "회원 정보를 입력하여 가입을 진행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "201 회원가입 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류 (BadRequestException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "IDC001", description = "409 중복된 ID (ConflictException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류 (ServerException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<String>> saveMember(@RequestBody SaveMemberRequest request) {
		// Request 유효성 검사
		request.validateSaveMemberRequest();

		memberService.saveMember(request.toSaveMemberRequestDto());

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(BaseResponse.ofSuccess(HttpStatus.CREATED.value(), "SUCCESS"));
	}

	@PostMapping("/login")
	@Operation(summary = "로그인 API", description = "회원 정보를 입력하여 로그인을 진행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류 (BadRequestException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류 (ServerException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "INF001", description = "404 존재하지 않는 아이디입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "IPMB002", description = "400 아이디 비밀번호가 틀렸습니다. (BadRequestException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<LoginMemberResponse>> loginMember(@RequestBody LoginMemberRequest request) {
		// Request 유효성 검사
		request.validateLoginMemberRequest();

		LoginMemberResponseDto responseDto = memberService.loginMember(request.toLoginMemberRequestDto());
		LoginMemberResponse response = memberControllerMapper.toLoginMemberResponse(responseDto);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@GetMapping()
	@Operation(summary = "모든 사용자 조회 API", description = "모든 사용자 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "사용자 조회 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류 (BadRequestException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류 (ServerException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "INF001", description = "404 존재하지 않는 아이디입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "TU001", description = "401 올바르지 않은 토큰입니다. (UnAuthorized Exception 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<List<GetMemberResponse>>> getAllMembers(HttpServletRequest servletRequest) {
		List<GetMemberResponse> responses = memberService.getAllMembers(servletRequest).stream()
			.map(memberControllerMapper::toGetMemberResponse)
			.collect(Collectors.toList());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), responses));
	}

	@GetMapping("/{id}")
	@Operation(summary = "특정 사용자 조회 API", description = "특정 사용자 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "사용자 조회 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류 (BadRequestException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류 (ServerException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "INF001", description = "404 존재하지 않는 아이디입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "TU001", description = "401 올바르지 않은 토큰입니다. (UnAuthorized Exception 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<GetMemberResponse>> getMember(
		@PathVariable(name = "id", required = true) String id,
		HttpServletRequest servletRequest
	) {
		GetMemberResponseDto responseDto = memberService.getMember(servletRequest, id);
		GetMemberResponse response = memberControllerMapper.toGetMemberResponse(responseDto);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}
}
