package assignment.application.infrastructure.loan.entity;

import java.time.LocalDate;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import assignment.application.infrastructure.book.entity.BookEntity;
import assignment.application.infrastructure.member.entity.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LOAN")
@DynamicInsert
@DynamicUpdate
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanEntity {
	@Id
	@Column(name = "loan_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long loanSeq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_seq")
	private MemberEntity memberEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_seq")
	private BookEntity bookEntity;

	@Column(nullable = false, name = "loan_date")
	private LocalDate loanDate;

	@Column(nullable = false, name = "due_date")
	private LocalDate dueDate;

	@Column(nullable = false, name = "return_date")
	private LocalDate returnDate;
}
