package assignment.presentation.book.vo.request;

import static assignment.application.service.book.dto.request.BookRequestDto.*;

import java.time.LocalDate;

import assignment.application.exception.message.ErrorResult;
import assignment.application.exception.status.BadRequestException;

public class BookRequest {
	public record SaveBookRequest(
		String title,
		String author,
		LocalDate publishedDate
	) {
		public void validateSaveBookRequest() {
			if (this.title == null || title.isEmpty()) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.author == null || author.isEmpty()) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.publishedDate == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}

		public SaveBookRequestDto toSaveBookRequestDto() {
			return new SaveBookRequestDto(
				this.title,
				this.author,
				this.publishedDate
			);
		}
	}

	public record ModifyBookRequest(
		Long bookSeq,
		String title,
		String author,
		LocalDate publishedDate
	) {
		public void validateModifyBookRequest() {
			if (this.bookSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.title == null || title.isEmpty()) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.author == null || author.isEmpty()) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.publishedDate == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}

		public ModifyBookRequestDto toModifyBookRequestDto() {
			return new ModifyBookRequestDto(
				this.bookSeq,
				this.title,
				this.author,
				this.publishedDate
			);
		}
	}

	public record DeleteBookRequest(
		Long bookSeq
	) {
		public void validateDeleteBookRequest() {
			if (this.bookSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}

		public DeleteBookRequestDto toDeleteBookRequestDto() {
			return new DeleteBookRequestDto(
				this.bookSeq
			);
		}
	}
}
