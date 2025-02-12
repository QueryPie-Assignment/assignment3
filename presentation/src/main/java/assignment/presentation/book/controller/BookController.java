package assignment.presentation.book.controller;

import static assignment.application.service.book.dto.response.BookResponseDto.*;
import static assignment.presentation.book.vo.request.BookRequest.*;
import static assignment.presentation.book.vo.response.BookResponse.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import assignment.application.exception.GlobalExceptionHandler;
import assignment.application.infrastructure.book.entity.BookTag;
import assignment.application.service.book.service.BookService;
import assignment.presentation.base.BaseResponse;
import assignment.presentation.book.mapper.BookControllerMapper;
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
@RequestMapping("/book")
@Tag(name = "도서")
public class BookController {
	private final BookControllerMapper bookControllerMapper;

	private final BookService bookService;

	@PostMapping()
	@Operation(summary = "도서 등록 API", description = "도서 정보를 입력하여 가입을 진행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "도서 저장 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류 (BadRequestException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류 (ServerException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "INF001", description = "404 존재하지 않는 아이디입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "TU001", description = "401 올바르지 않은 토큰입니다. (UnAuthorized Exception 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "BTN003", description = "404 존재하지 않는 Book Tag입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<SaveBookResponse>> saveBook(
		@Valid
		@RequestBody SaveBookRequest request,
		@RequestParam(name = "bookTag") BookTag bookTag,
		HttpServletRequest servletRequest
	) {
		// Request 유효성 검사
		request.validateSaveBookRequest();

		SaveBookResponseDto responseDto = bookService.saveBook(servletRequest, request.toSaveBookRequestDto(), bookTag);
		SaveBookResponse response = bookControllerMapper.toSaveBookResponse(responseDto);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(BaseResponse.ofSuccess(HttpStatus.CREATED.value(), response));
	}

	@GetMapping("/{bookSeq}")
	@Operation(summary = "특정 도서 조회 API", description = "특정 도서 조회를 진행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 조회 성공",
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
	public ResponseEntity<BaseResponse<GetBookResponse>> getBook(
		@PathVariable(name = "bookSeq", required = true) Long bookSeq,
		HttpServletRequest servletRequest
	) {
		GetBookResponseDto responseDto = bookService.getBook(servletRequest, bookSeq);
		GetBookResponse response = bookControllerMapper.toGetBookResponse(responseDto);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@GetMapping()
	@Operation(summary = "모든 도서 조회 API", description = "모든 도서 조회를 진행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 조회 성공",
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
	public ResponseEntity<BaseResponse<Page<GetBookResponse>>> getAllBooks(
		HttpServletRequest servletRequest,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);

		Page<GetBookResponseDto> responseDto = bookService.getAllBooks(servletRequest, pageable);
		Page<GetBookResponse> response = responseDto.map(bookControllerMapper::toGetBookResponse);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@GetMapping("/tag")
	@Operation(summary = "태그 기준 도서 조회 API", description = "태그 기준 도서 조회를 진행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 조회 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류 (BadRequestException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류 (ServerException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "INF001", description = "404 존재하지 않는 아이디입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "TU001", description = "401 올바르지 않은 토큰입니다. (UnAuthorized Exception 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "BTN003", description = "404 존재하지 않는 Book Tag입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<List<GetBookResponse>>> getBooksByTag(
		HttpServletRequest servletRequest,
		@RequestParam(name = "bookTag", required = true) BookTag bookTag
	) {
		List<GetBookResponseDto> responseDto = bookService.getBooksByTag(servletRequest, bookTag);
		List<GetBookResponse> response = responseDto.stream()
			.map(bookControllerMapper::toGetBookResponse)
			.collect(Collectors.toList());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@GetMapping("/search")
	@Operation(summary = "저자 이름으로 도서 조회 API", description = "저자 이름으로 조회를 진행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 조회 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류 (BadRequestException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류 (ServerException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "INF001", description = "404 존재하지 않는 아이디입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "TU001", description = "401 올바르지 않은 토큰입니다. (UnAuthorized Exception 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "BTN003", description = "404 존재하지 않는 Book Tag입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<List<GetBookResponse>>> getBooksByAuthor(
		HttpServletRequest servletRequest,
		@RequestParam(name = "author", required = true) String author
	) {
		List<GetBookResponseDto> responseDto = bookService.getBooksByAuthor(servletRequest, author);
		List<GetBookResponse> response = responseDto.stream()
			.map(bookControllerMapper::toGetBookResponse)
			.collect(Collectors.toList());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@PutMapping()
	@Operation(summary = "도서 수정 API", description = "도서 수정을 진행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 수정 성공",
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
		@ApiResponse(responseCode = "BTN003", description = "404 존재하지 않는 Book Tag입니다. (NotFoundException 발생)",
			content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<String>> modifyBook(
		@Valid
		@RequestBody ModifyBookRequest request,
		@RequestParam(name = "bookTag") BookTag bookTag,
		HttpServletRequest servletRequest
	) {
		// Request 유효성 검사
		request.validateModifyBookRequest();

		bookService.modifyBook(servletRequest, request.toModifyBookRequestDto(), bookTag);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}

	@DeleteMapping()
	@Operation(summary = "도서 삭제 API", description = "도서 삭제를 진행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 삭제 성공",
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
	public ResponseEntity<BaseResponse<String>> deleteBook(
		@Valid
		@RequestBody DeleteBookRequest request,
		HttpServletRequest servletRequest
	) {
		// Request 유효성 검사
		request.validateDeleteBookRequest();

		bookService.deleteBook(servletRequest, request.toDeleteBookRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}
}
