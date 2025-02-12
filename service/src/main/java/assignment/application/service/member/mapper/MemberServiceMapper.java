package assignment.application.service.member.mapper;

import static assignment.application.service.member.dto.request.MemberRequestDto.*;
import static assignment.application.service.member.dto.response.MemberResponseDto.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import assignment.application.infrastructure.member.entity.MemberEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberServiceMapper {
	@Mapping(target = "memberSeq", ignore = true)
	@Mapping(target = "id", source = "requestDto.id")
	@Mapping(target = "password", source = "encodedPassword")
	@Mapping(target = "userName", source = "requestDto.userName")
	MemberEntity toMemberEntity(SaveMemberRequestDto requestDto, String encodedPassword);

	@Mapping(target = "id", source = "memberEntity.id")
	@Mapping(target = "userName", source = "memberEntity.userName")
	GetMemberResponseDto toGetMemberResponseDto(MemberEntity memberEntity);
}
