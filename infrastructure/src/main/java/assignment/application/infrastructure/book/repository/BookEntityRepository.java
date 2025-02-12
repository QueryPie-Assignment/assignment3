package assignment.application.infrastructure.book.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import assignment.application.infrastructure.book.entity.BookEntity;
import assignment.application.infrastructure.book.entity.BookTag;

@Component
public interface BookEntityRepository {
	BookEntity save(BookEntity bookEntity);

	BookEntity findBySeq(Long bookSeq);

	Page<BookEntity> findAllBooks(Pageable pageable);

	List<BookEntity> findBooksByTag(BookTag bookTag);

	void deleteBook(BookEntity bookEntity);

	List<BookEntity> findByAuthor(String author);
}
