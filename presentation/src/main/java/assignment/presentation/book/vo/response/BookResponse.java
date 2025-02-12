package assignment.presentation.book.vo.response;

import java.time.LocalDate;

import assignment.application.infrastructure.book.entity.BookTag;

public class BookResponse {
	public record SaveBookResponse(
		Long bookSeq
	) {

	}

	public record GetBookResponse(
		Long bookSeq,
		String title,
		String author,
		LocalDate publishedDate,
		Boolean isAvailable,
		BookTag bookTag
	) {

	}
}
