package assignment.application.service.book.service;

import static assignment.application.service.book.dto.request.BookRequestDto.*;
import static assignment.application.service.book.dto.response.BookResponseDto.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import assignment.application.domain.book.BookDomain;
import assignment.application.domain.member.MemberDomain;
import assignment.application.infrastructure.book.entity.BookEntity;
import assignment.application.infrastructure.book.entity.BookTag;
import assignment.application.infrastructure.book.repository.BookEntityRepository;
import assignment.application.infrastructure.member.repository.MemberEntityRepository;
import assignment.application.service.book.mapper.BookServiceMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
	private final BookEntityRepository bookEntityRepository;
	private final MemberEntityRepository memberEntityRepository;

	private final BookServiceMapper bookServiceMapper;

	private final MemberDomain memberDomain;
	private final BookDomain bookDomain;

	@Transactional
	@CacheEvict(value = {"booksByAuthor"}, allEntries = true)
	public SaveBookResponseDto saveBook(
		HttpServletRequest servletRequest,
		SaveBookRequestDto requestDto,
		BookTag bookTag
	) {
		/*
		 유효성 검사

		 1. Token 유효성 검사
		 2. Member 유효성 검사
		 3. Book Tag 유효성 검사
		*/
		String id = memberDomain.extractIdFromRequest(servletRequest);
		memberEntityRepository.findById(id);
		bookDomain.validateBookTag(bookTag);

		BookEntity bookEntity = bookServiceMapper.toBookEntity(requestDto);
		bookEntityRepository.save(bookEntity);

		return new SaveBookResponseDto(bookEntity.getBookSeq());
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "book", key = "#bookSeq")
	public GetBookResponseDto getBook(HttpServletRequest servletRequest, Long bookSeq) {
		/*
		 유효성 검사

		 1. Token 유효성 검사
		 2. Member 유효성 검사
		 3. Book 유효성 검사
		*/
		String id = memberDomain.extractIdFromRequest(servletRequest);
		memberEntityRepository.findById(id);
		BookEntity bookEntity = bookEntityRepository.findBySeq(bookSeq);

		return bookServiceMapper.toGetBookResponseDto(bookEntity);
	}

	@Transactional(readOnly = true)
	public Page<GetBookResponseDto> getAllBooks(HttpServletRequest servletRequest, Pageable pageable) {
		/*
		 유효성 검사

		 1. Token 유효성 검사
		 2. Member 유효성 검사
		*/
		String id = memberDomain.extractIdFromRequest(servletRequest);
		memberEntityRepository.findById(id);

		Page<BookEntity> bookEntityPage = bookEntityRepository.findAllBooks(pageable);

		return bookEntityPage.map(bookServiceMapper::toGetBookResponseDto);
	}

	@Transactional(readOnly = true)
	public List<GetBookResponseDto> getBooksByTag(HttpServletRequest servletRequest, BookTag bookTag) {
		/*
		 유효성 검사

		 1. Token 유효성 검사
		 2. Member 유효성 검사
		*/
		String id = memberDomain.extractIdFromRequest(servletRequest);
		memberEntityRepository.findById(id);

		return bookEntityRepository.findBooksByTag(bookTag).stream()
			.map(bookServiceMapper::toGetBookResponseDto)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "booksByAuthor", key = "#author")
	public List<GetBookResponseDto> getBooksByAuthor(HttpServletRequest servletRequest, String author) {
		/*
		 유효성 검사

		 1. Token 유효성 검사
		 2. Member 유효성 검사
		*/
		String id = memberDomain.extractIdFromRequest(servletRequest);
		memberEntityRepository.findById(id);

		// Book Entity 조회
		return bookEntityRepository.findByAuthor(author).stream()
			.map(bookServiceMapper::toGetBookResponseDto)
			.collect(Collectors.toList());
	}

	@Transactional
	@CacheEvict(value = {"book", "booksByAuthor"}, key = "#requestDto.bookSeq()")
	public void modifyBook(HttpServletRequest servletRequest, ModifyBookRequestDto requestDto, BookTag bookTag) {
		/*
		 유효성 검사

		 1. Token 유효성 검사
		 2. Member 유효성 검사
		 3. Book 유효성 검사
		*/
		String id = memberDomain.extractIdFromRequest(servletRequest);
		memberEntityRepository.findById(id);
		BookEntity bookEntity = bookEntityRepository.findBySeq(requestDto.bookSeq());
		bookDomain.validateBookTag(bookTag);

		BookEntity updateBookEntity = bookEntity.updateBookEntity(
			requestDto.title(),
			requestDto.author(),
			requestDto.publishedDate(),
			bookTag
		);

		bookEntityRepository.save(updateBookEntity);
	}

	@Transactional
	@CacheEvict(value = {"book", "booksByAuthor"}, key = "#requestDto.bookSeq()")
	public void deleteBook(HttpServletRequest servletRequest, DeleteBookRequestDto requestDto) {
		/*
		 유효성 검사

		 1. Token 유효성 검사
		 2. Member 유효성 검사
		 3. Book 유효성 검사
		*/
		String id = memberDomain.extractIdFromRequest(servletRequest);
		memberEntityRepository.findById(id);
		BookEntity bookEntity = bookEntityRepository.findBySeq(requestDto.bookSeq());

		bookEntityRepository.deleteBook(bookEntity);
	}
}
