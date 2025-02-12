package assignment.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import assignment.application.exception.message.ErrorResult;
import assignment.application.exception.status.BadRequestException;
import assignment.application.exception.status.ConflictException;
import assignment.application.exception.status.NotFoundRequestException;
import assignment.application.exception.status.UnAuthorizedException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({Exception.class})
	public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
		log.error("❌ Server Exception occur: ", exception);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				exception.getMessage() != null ? exception.getMessage() : "Internal Server Error",
				"S500"
			));
	}

	@ExceptionHandler({BadRequestException.class})
	public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException exception) {
		log.error("⚠️ BadRequest Exception occur: ", exception);

		return makeErrorResponseEntity(exception.getErrorResult());
	}

	@ExceptionHandler({NotFoundRequestException.class})
	public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundRequestException exception) {
		log.error("🔍 Not Found Exception occur: ", exception);

		return makeErrorResponseEntity(exception.getErrorResult());
	}

	@ExceptionHandler({ConflictException.class})
	public ResponseEntity<ErrorResponse> handleConflictException(final ConflictException exception) {
		log.error("🔄 Conflict Exception occur: ", exception);

		return makeErrorResponseEntity(exception.getErrorResult());
	}

	@ExceptionHandler({UnAuthorizedException.class})
	public ResponseEntity<ErrorResponse> handleUnAuthorizedException(final UnAuthorizedException exception) {
		log.error("🔑 Unauthorized Exception occur: ", exception);

		return makeErrorResponseEntity(exception.getErrorResult());
	}

	private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final ErrorResult errorResult) {
		return ResponseEntity.status(errorResult.getHttpStatus())
			.body(new ErrorResponse(
				errorResult.getHttpStatus(),
				errorResult.getMessage(),
				errorResult.getCode()
			));
	}

	@Getter
	@RequiredArgsConstructor
	public static class ErrorResponse {
		private final int status;
		private final String message;
		private final String code;
	}
}
