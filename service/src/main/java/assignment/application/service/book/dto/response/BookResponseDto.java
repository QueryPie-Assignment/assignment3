package assignment.application.service.book.dto.response;

import java.time.LocalDate;

import assignment.application.infrastructure.book.entity.BookTag;

public class BookResponseDto {
	public record SaveBookResponseDto(
		Long bookSeq
	) {

	}

	public record GetBookResponseDto(
		Long bookSeq,
		String title,
		String author,
		LocalDate publishedDate,
		Boolean isAvailable,
		BookTag bookTag
	) {

	}
}
