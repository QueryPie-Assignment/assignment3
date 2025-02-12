package assignment.presentation.member.mapper;

import static assignment.application.service.member.dto.response.MemberResponseDto.*;
import static assignment.presentation.member.vo.response.MemberResponse.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberControllerMapper {
	LoginMemberResponse toLoginMemberResponse(LoginMemberResponseDto responseDto);

	ReissueTokenResponse toReissueTokenResponse(ReissueTokenResponseDto responseDto);

	GetMemberResponse toGetMemberResponse(GetMemberResponseDto responseDto);
}
