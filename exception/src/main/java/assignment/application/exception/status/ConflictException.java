package assignment.application.exception.status;

import assignment.application.exception.message.ErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ConflictException extends RuntimeException {
	private final ErrorResult errorResult;
}
