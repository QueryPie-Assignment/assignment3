package assignment.presentation.book.mapper;

import static assignment.application.service.book.dto.response.BookResponseDto.*;
import static assignment.presentation.book.vo.response.BookResponse.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookControllerMapper {
	SaveBookResponse toSaveBookResponse(SaveBookResponseDto responseDto);

	GetBookResponse toGetBookResponse(GetBookResponseDto responseDto);
}
