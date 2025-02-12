package assignment.application.exception.status;

import assignment.application.exception.message.ErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotFoundRequestException extends RuntimeException {
	private final ErrorResult errorResult;
}
