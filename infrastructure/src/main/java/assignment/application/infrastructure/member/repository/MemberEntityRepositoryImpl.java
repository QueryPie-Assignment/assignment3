package assignment.application.infrastructure.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import assignment.application.exception.message.ErrorResult;
import assignment.application.exception.status.BadRequestException;
import assignment.application.exception.status.ConflictException;
import assignment.application.infrastructure.member.entity.MemberEntity;
import assignment.application.infrastructure.member.entity.QMemberEntity;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberEntityRepositoryImpl implements MemberEntityRepository {
	private final MemberEntityJpaRepository memberEntityJpaRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public MemberEntity save(MemberEntity memberEntity) {
		return memberEntityJpaRepository.save(memberEntity);
	}

	@Override
	public void isDuplicatedId(String id) {
		memberEntityJpaRepository.findById(id)
			.ifPresent(entity -> {
				throw new ConflictException(ErrorResult.ID_DUPLICATION_CONFLICT_EXCEPTION);
			});
	}

	@Override
	public MemberEntity findById(String id) {
		QMemberEntity qMemberEntity = QMemberEntity.memberEntity;

		return Optional.ofNullable(
			queryFactory
				.selectFrom(qMemberEntity)
				.where(
					qMemberEntity.id.eq(id)
				)
				.fetchOne()
		).orElseThrow(() -> new BadRequestException(ErrorResult.ID_NOT_FOUND_EXCEPTION));
	}

	@Override
	public List<MemberEntity> findAll() {
		QMemberEntity qMemberEntity = QMemberEntity.memberEntity;

		return queryFactory
			.selectFrom(qMemberEntity)
			.fetch();
	}
}
