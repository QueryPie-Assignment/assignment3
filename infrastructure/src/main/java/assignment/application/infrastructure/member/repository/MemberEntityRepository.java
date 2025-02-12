package assignment.application.infrastructure.member.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import assignment.application.infrastructure.member.entity.MemberEntity;

@Component
public interface MemberEntityRepository {
	MemberEntity save(MemberEntity memberEntity);

	void isDuplicatedId(String id);

	MemberEntity findById(String id);

	List<MemberEntity> findAll();
}
