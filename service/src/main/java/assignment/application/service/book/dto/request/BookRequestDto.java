package assignment.application.service.book.dto.request;

import java.time.LocalDate;

import assignment.application.infrastructure.book.entity.BookTag;

public class BookRequestDto {
	public record SaveBookRequestDto(
		String title,
		String author,
		LocalDate publishedDate
	) {

	}

	public record ModifyBookRequestDto(
		Long bookSeq,
		String title,
		String author,
		LocalDate publishedDate
	) {

	}

	public record DeleteBookRequestDto(
		Long bookSeq
	) {

	}
}
