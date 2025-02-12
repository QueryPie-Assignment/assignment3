package assignment.application.service.book.mapper;

import static assignment.application.service.book.dto.request.BookRequestDto.*;
import static assignment.application.service.book.dto.response.BookResponseDto.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import assignment.application.infrastructure.book.entity.BookEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookServiceMapper {
	@Mapping(target = "bookSeq", ignore = true)
	BookEntity toBookEntity(SaveBookRequestDto requestDto);

	GetBookResponseDto toGetBookResponseDto(BookEntity bookEntity);
}
