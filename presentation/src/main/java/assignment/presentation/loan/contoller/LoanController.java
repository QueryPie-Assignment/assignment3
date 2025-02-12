package assignment.presentation.loan.contoller;

import static assignment.application.service.loan.dto.response.LoanResponseDto.*;
import static assignment.presentation.loan.vo.request.LoanRequest.*;
import static assignment.presentation.loan.vo.response.LoanResponse.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import assignment.application.exception.GlobalExceptionHandler;
import assignment.application.service.loan.service.LoanService;
import assignment.presentation.base.BaseResponse;
import assignment.presentation.loan.mapper.LoanControllerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan")
@Tag(name = "대출")
public class LoanController {
	private final LoanService loanService;

	private final LoanControllerMapper loanControllerMapper;

	@PostMapping()
	@Operation(summary = "대출 등록 API", description = "도서 정보를 입력하여 대출을 진행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "대출 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류 (BadRequestException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류 (ServerException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "INF001", description = "404 존재하지 않는 아이디입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "TU001", description = "401 올바르지 않은 토큰입니다. (UnAuthorized Exception 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "BSN002", description = "404 존재하지 않는 Book Seq입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "ALB003", description = "400 이미 대출 받은 책 입니다. (BadRequestException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<String>> saveLoan(
		@Valid
		@RequestBody SaveLoanRequest request,
		HttpServletRequest servletRequest
	) {
		// Request 유효성 검사
		request.validateSaveLoanRequest();

		loanService.saveLoan(servletRequest, request.toSaveLoanRequestDto());

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(BaseResponse.ofSuccess(HttpStatus.CREATED.value(), "SUCCESS"));
	}

	@GetMapping()
	@Operation(summary = "대출 상태 확인 API", description = "도서 정보를 입력하여 대출 상태 확인을 진행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "대출 상태 확인 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류 (BadRequestException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류 (ServerException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "INF001", description = "404 존재하지 않는 아이디입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "TU001", description = "401 올바르지 않은 토큰입니다. (UnAuthorized Exception 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "BSN002", description = "404 존재하지 않는 Book Seq입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<CheckLoanResponse>> checkLoan(
		@Valid
		@RequestParam(name = "bookSeq") Long bookSeq,
		HttpServletRequest servletRequest
	) {
		CheckLoanResponseDto responseDto = loanService.checkLoan(servletRequest, bookSeq);
		CheckLoanResponse response = loanControllerMapper.toCheckLoanResponse(responseDto);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@DeleteMapping()
	@Operation(summary = "도서 반납 API", description = "도서 정보를 입력하여 도서 반납을 진행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 반납 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류 (BadRequestException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류 (ServerException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "INF001", description = "404 존재하지 않는 아이디입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "TU001", description = "401 올바르지 않은 토큰입니다. (UnAuthorized Exception 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "BSN002", description = "404 존재하지 않는 Book Seq입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "LBN004", description = "404 해당 회원이 빌린 책이 없습니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<String>> deleteLoan(
		@Valid
		@RequestBody DeleteLoanRequest request,
		HttpServletRequest servletRequest
	) {
		// Request 유효성 검사
		request.validateDeleteLoanRequest();

		loanService.deleteLoan(servletRequest, request.toDeleteLoanRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}
}
