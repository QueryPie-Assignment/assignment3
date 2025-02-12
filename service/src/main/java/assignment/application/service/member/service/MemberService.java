package assignment.application.service.member.service;

import static assignment.application.service.member.dto.request.MemberRequestDto.*;
import static assignment.application.service.member.dto.response.MemberResponseDto.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import assignment.application.domain.member.MemberDomain;
import assignment.application.infrastructure.member.entity.MemberEntity;
import assignment.application.infrastructure.member.repository.MemberEntityRepository;
import assignment.application.service.member.mapper.MemberServiceMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberEntityRepository memberEntityRepository;

	private final MemberDomain memberDomain;

	private final MemberServiceMapper memberServiceMapper;

	private final BCryptPasswordEncoder passwordEncoder;

	@Transactional
	public void saveMember(SaveMemberRequestDto requestDto) {
		/*
		 유효성 검사

		 1. Member id 중복 확인
		*/
		memberEntityRepository.isDuplicatedId(requestDto.id());

		// 비밀번호 암호화
		MemberEntity memberEntity
			= memberServiceMapper.toMemberEntity(requestDto, passwordEncoder.encode(requestDto.password()));

		memberEntityRepository.save(memberEntity);
	}

	@Transactional
	public LoginMemberResponseDto loginMember(LoginMemberRequestDto requestDto) {
		/*
		 유효성 검사

		 1. Member id 존재 확인
		 2. id - password 일치 여부 확인
		*/
		MemberEntity memberEntity = memberEntityRepository.findById(requestDto.id());
		memberDomain
			.checkIdPassword(requestDto.id(), requestDto.password(), memberEntity.getId(), memberEntity.getPassword());

		// Jwt Token 생성
		String accessToken = memberDomain.generateAccessToken(requestDto.id());
		String refreshToken = memberDomain.generateRefreshToken(requestDto.id());

		// Jwt Token 저장
		memberDomain.saveRefreshToken(refreshToken, requestDto.id());

		return new LoginMemberResponseDto(accessToken, refreshToken);
	}

	@Transactional(readOnly = true)
	public List<GetMemberResponseDto> getAllMembers(HttpServletRequest servletRequest) {
		/*
		 유효성 검사

		 1. Token 추출
		 2. Member id 존재 확인
		*/
		String id = memberDomain.extractIdFromRequest(servletRequest);
		memberEntityRepository.findById(id);

		return memberEntityRepository.findAll().stream()
			.map(memberServiceMapper::toGetMemberResponseDto)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public GetMemberResponseDto getMember(HttpServletRequest servletRequest, String id) {
		/*
		 유효성 검사

		 1. Token 추출
		 2. Member headerId 존재 확인
		 3. Member id 존재 확인
		*/
		String headerId = memberDomain.extractIdFromRequest(servletRequest);
		memberEntityRepository.findById(headerId);
		MemberEntity memberEntity = memberEntityRepository.findById(id);

		return memberServiceMapper.toGetMemberResponseDto(memberEntity);
	}
}
