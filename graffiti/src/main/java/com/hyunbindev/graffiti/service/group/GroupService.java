package com.hyunbindev.graffiti.service.group;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunbindev.graffiti.constant.exception.MemberExceptionConst;
import com.hyunbindev.graffiti.data.group.CreateGroupDTO;
import com.hyunbindev.graffiti.entity.GroupEntity;
import com.hyunbindev.graffiti.entity.MemberEntity;
import com.hyunbindev.graffiti.exception.CommonAPIException;
import com.hyunbindev.graffiti.repository.GroupRepository;
import com.hyunbindev.graffiti.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {
	private final GroupRepository groupRepository;
	private final MemberRepository memberRepository;
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
	public void joinGroup(String userUuid, String groupUuid, String inviteCode) {
		MemberEntity member = memberRepository.findById(userUuid)
				.orElseThrow(()-> new CommonAPIException(MemberExceptionConst.NOT_FOUND));
		
		GroupEntity group = groupRepository.findById(groupUuid)
				.orElseThrow(()-> new CommonAPIException("그룹을 찾을 수 없습니다.",HttpStatus.NOT_FOUND));
		
		member.getGroups().add(group);
	}
	
	public String createInviteCode(String userUuid, String groupUuid) {
		return "code";
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
