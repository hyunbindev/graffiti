package com.hyunbindev.graffiti.service.member;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunbindev.graffiti.config.oauth.KakaoOauth2User;
import com.hyunbindev.graffiti.constant.Role;
import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.data.member.JoinGroupsDTO;
import com.hyunbindev.graffiti.data.member.MemberInfoDTO;
import com.hyunbindev.graffiti.entity.jpa.group.GroupEntity;
import com.hyunbindev.graffiti.entity.jpa.group.MemberGroupLinkedEntity;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.jpa.MemberRepository;
import com.hyunbindev.graffiti.repository.jpa.group.GroupRepository;
import com.hyunbindev.graffiti.repository.jpa.group.MemberGroupLinkedRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
	private final MemberRepository memberRepository;
	private final GroupRepository groupRepository;
	private final MemberGroupLinkedRepository memberGroupLinkedRepository;
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
	/**
	 * 회원 정보 조회
	 * @param userUuid
	 * @return memberInfoDTO
	 */
	@Transactional(readOnly = true)
	public MemberInfoDTO getMemberInfo(String userUuid) {
		
		MemberEntity member = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		return new MemberInfoDTO(member);
	}
	/**
	 * 소속 그룹 조회
	 * @param userUuid
	 * @return
	 */
	public List<JoinGroupsDTO> getGroup(String userUuid){
		MemberEntity member = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		List<GroupEntity> groups = member.getGroupLinks().stream()
				.map((link)->link.getGroup())
				.toList(); 
		return groups.stream().map((entity)-> JoinGroupsDTO.mappingDTO(entity)).toList();
	}
	/**
	 * 소속 그룹 멤버 조회
	 * @param userUuid
	 * @param groupId
	 * @return
	 */
	public List<MemberInfoDTO> getMembersIngroup(String userUuid, String groupId, String keyWord){
		MemberEntity member = memberRepository.findById(userUuid)
				.orElseThrow(()->new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		GroupEntity group = groupRepository.findById(groupId)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.UNAUTHORIZED));
		
		if(keyWord!=null)
			keyWord = "%"+keyWord+"%";
		
		if(!member.isInGroup(group))
			throw new CommonAPIException(MemberExceptionConst.UNAUTHORIZED);
		
		List<MemberGroupLinkedEntity> membersIngroup = memberGroupLinkedRepository.findByGroupFetchJoin(group, keyWord);
		
		return membersIngroup.stream()
				.map((gl)->new MemberInfoDTO(gl.getMember()))
				.toList();
	}
}