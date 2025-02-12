package assignment.application.domain.book;

import java.util.EnumSet;

import org.springframework.stereotype.Component;

import assignment.application.exception.message.ErrorResult;
import assignment.application.exception.status.BadRequestException;
import assignment.application.infrastructure.book.entity.BookTag;

@Component
public class BookDomain {
	public void validateBookTag(BookTag bookTag) {
		if (!EnumSet.allOf(BookTag.class).contains(bookTag)) {
			throw new BadRequestException(ErrorResult.BOOK_TAG_NOT_FOUND_EXCEPTION);
		}
	}

	public void validateIsAvailable(Boolean isAvailable) {
		if(!isAvailable) {
			throw new BadRequestException(ErrorResult.ALREADY_LOAN_BAD_REQUEST_EXCEPTION);
		}
	}
}
