package assignment.application.infrastructure.book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

import assignment.application.exception.message.ErrorResult;
import assignment.application.exception.status.NotFoundRequestException;
import assignment.application.infrastructure.book.entity.BookEntity;
import assignment.application.infrastructure.book.entity.BookTag;
import assignment.application.infrastructure.book.entity.QBookEntity;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookEntityRepositoryImpl implements BookEntityRepository {
	private final BookEntityJpaRepository bookEntityJpaRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public BookEntity save(BookEntity bookEntity) {
		return bookEntityJpaRepository.save(bookEntity);
	}

	@Override
	public BookEntity findBySeq(Long bookSeq) {
		QBookEntity qBookEntity = QBookEntity.bookEntity;

		return Optional.ofNullable(
			queryFactory
				.selectFrom(qBookEntity)
				.where(
					qBookEntity.bookSeq.eq(bookSeq)
				).fetchOne()
		).orElseThrow(() -> new NotFoundRequestException(ErrorResult.BOOK_SEQ_NOT_FOUND_EXCEPTION));
	}

	@Override
	public Page<BookEntity> findAllBooks(Pageable pageable) {
		QBookEntity qBookEntity = QBookEntity.bookEntity;

		QueryResults<BookEntity> results = queryFactory
			.selectFrom(qBookEntity)
			.orderBy(qBookEntity.publishedDate.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		List<BookEntity> bookEntities = results.getResults();
		long total = results.getTotal();

		return new PageImpl<>(bookEntities, pageable, total);
	}

	@Override
	public List<BookEntity> findBooksByTag(BookTag bookTag) {
		QBookEntity qBookEntity = QBookEntity.bookEntity;

		return queryFactory
			.selectFrom(qBookEntity)
			.where(
				qBookEntity.bookTag.eq(bookTag)
			).fetch();
	}

	@Override
	public void deleteBook(BookEntity bookEntity) {
		bookEntityJpaRepository.delete(bookEntity);
	}

	@Override
	public List<BookEntity> findByAuthor(String author) {
		QBookEntity qBookEntity = QBookEntity.bookEntity;

		return queryFactory
			.selectFrom(qBookEntity)
			.where(
				qBookEntity.author.eq(author)
			).fetch();
	}
}
