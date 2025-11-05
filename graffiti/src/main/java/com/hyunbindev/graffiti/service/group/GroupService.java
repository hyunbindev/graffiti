package com.hyunbindev.graffiti.service.group;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.data.group.CreateGroupDTO;
import com.hyunbindev.graffiti.data.group.JoinGroupDTO;
import com.hyunbindev.graffiti.entity.jpa.GroupEntity;
import com.hyunbindev.graffiti.entity.jpa.MemberEntity;
import com.hyunbindev.graffiti.entity.redis.group.GroupInviteCodeEntity;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.jpa.GroupRepository;
import com.hyunbindev.graffiti.repository.jpa.MemberRepository;
import com.hyunbindev.graffiti.repository.redis.group.GroupInviteCodeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {
	private final GroupRepository groupRepository;
	private final MemberRepository memberRepository;
	private final GroupInviteCodeRepository groupInviteCodeRepository;
	/**
	 * Group 생성
	 * @param userUuid
	 * @param groupName
	 */
	@Transactional
	public void createGroupRepository(String userUuid, CreateGroupDTO createDto) {
		MemberEntity member = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		GroupEntity group = new GroupEntity();
		group.setName(createDto.getGroupName());
		
		group = groupRepository.save(group);
		
		member.getGroups().add(group);
	}
	/**
	 * 그룹 참여
	 * @author hyunbinDev
	 * @param userUuid
	 * @param groupUuid
	 * @param inviteCode
	 */
	@Transactional
	public void joinGroup(String userUuid, JoinGroupDTO joinDto) {
		MemberEntity member = memberRepository.findByIdWithGroups(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		GroupInviteCodeEntity codeEntity = groupInviteCodeRepository.findById(joinDto.getCode())
				.orElseThrow(()-> new CommonAPIException("기간이 만료된 코드 입니다.",HttpStatus.NOT_FOUND));
		
		
		
		GroupEntity group = groupRepository.findById(codeEntity.getGroupUuid())
				.orElseThrow(()-> new CommonAPIException("그룹을 찾을 수 없습니다.",HttpStatus.NOT_FOUND));
		
		//이미 그룹 참여시 예외 처리
		if(member.getGroups().contains(group)) throw new CommonAPIException("이미 그룹에 참여하고 있습니다.",HttpStatus.CONFLICT);
		
		member.getGroups().add(group);
	}
	/**
	 * 초대 코드 생성
	 * @param userUuid
	 * @param groupUuid
	 * @return
	 */
	@Transactional(readOnly=true)
	public String createInviteCode(String userUuid, String groupUuid) {
		MemberEntity member = memberRepository.findByIdWithGroups(userUuid)
				.orElseThrow(()->new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		GroupEntity group = groupRepository.findById(groupUuid)
				.orElseThrow(()-> new CommonAPIException("그룹을 찾을 수 없습니다.",HttpStatus.NOT_FOUND));
		
		//그룹에 가입하지 않은 사용자에 대해 예외 처리
		if(!member.getGroups().contains(group)) throw new CommonAPIException("그룹 초대 권한이 없습니다.",HttpStatus.UNAUTHORIZED);
		
		UUID invitedCode = UUID.randomUUID();
		
		GroupInviteCodeEntity groupInviteCodeEntity = GroupInviteCodeEntity.builder()
				.code(invitedCode.toString())
				.groupName(group.getName())
				.groupUuid(group.getId())
				.build();
		
		groupInviteCodeEntity = groupInviteCodeRepository.save(groupInviteCodeEntity);
		
		return groupInviteCodeEntity.getCode();
	}
	/**
	 * 그룹 탈퇴
	 * @author hyunbinDev
	 * @param userUuid
	 * @param groupUuid
	 */
	@Transactional
	public void outGroup(String userUuid, String groupUuid) {
		MemberEntity member = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		boolean removed = member.getGroups().removeIf(group -> group.getId().equals(groupUuid));
		
		if(!removed) {
			log.warn("Group not found in member's list or already removed. userUuid: {} groupUuid {}", userUuid, groupUuid);
			throw new CommonAPIException("이미 탈퇴가 완료 되었습니다.", HttpStatus.CONFLICT);
		}
	}
}
