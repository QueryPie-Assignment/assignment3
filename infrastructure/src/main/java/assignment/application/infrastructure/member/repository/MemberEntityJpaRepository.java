package assignment.application.infrastructure.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import assignment.application.infrastructure.member.entity.MemberEntity;

@Repository
public interface MemberEntityJpaRepository extends JpaRepository<MemberEntity, Long> {
	Optional<MemberEntity> findById(String id);
}
