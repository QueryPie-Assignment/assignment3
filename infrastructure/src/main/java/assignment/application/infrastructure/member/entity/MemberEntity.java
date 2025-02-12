package assignment.application.infrastructure.member.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEMBER")
@DynamicInsert
@DynamicUpdate
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {
	@Id
	@Column(name = "member_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberSeq;

	@Column(nullable = false, unique = true, name = "id")
	private String id;

	@Column(nullable = false, name = "password")
	private String password;

	@Column(nullable = false, name = "user_name")
	private String userName;
}
