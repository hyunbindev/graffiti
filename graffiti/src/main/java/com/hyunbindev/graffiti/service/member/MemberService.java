package com.hyunbindev.graffiti.service.member;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hyunbindev.graffiti.config.oauth.KakaoOauth2User;
import com.hyunbindev.graffiti.constant.Role;
import com.hyunbindev.graffiti.entity.MemberEntity;
import com.hyunbindev.graffiti.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
	private final MemberRepository memberRepository;
	
	/**
	 * 회원 여부 인증 절차
	 * @author hyunbinDev
	 * @param user
	 * @return KakaoOauth2User
	 */
	@Transactional
	public KakaoOauth2User assignMember(KakaoOauth2User user) {
		Optional<MemberEntity> optionalMember = memberRepository.findByOauthKey(user.getName());
		//신규 회원 가입
		if(optionalMember.isEmpty()) {
			MemberEntity member = MemberEntity.builder()
					.oauthKey(user.getName())
					.nickName(user.getNickName())
					.profileImg(user.getProfileUrl())
					.lastLogin(user.getConnectedTime())
					.build();
			member = memberRepository.save(member);
			user.setAuthority(Role.MEMBER);
			user.setMemberUuid(member.getId());
			return user;
		}
		//회원 정보 업데이트
		MemberEntity member = optionalMember.get();
		user.setAuthority(Role.MEMBER);
		user.setMemberUuid(member.getId());
		return user;
	}
}