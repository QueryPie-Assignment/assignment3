package assignment.application.infrastructure.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import assignment.application.infrastructure.book.entity.BookEntity;

@Repository
public interface BookEntityJpaRepository extends JpaRepository<BookEntity, Long> {

}
