package com.hyunbindev.graffiti.controller.group;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyunbindev.graffiti.data.group.CreateGroupDTO;
import com.hyunbindev.graffiti.data.group.JoinGroupDTO;
import com.hyunbindev.graffiti.service.group.GroupService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class GroupController {
	private final GroupService groupService;
	/**
	 * 그룹 생성
	 * @param auth
	 * @param createDto
	 * @return
	 */
	@PostMapping("/v1/group")
	public ResponseEntity<Void> createGroup(Authentication auth, @RequestBody @Valid CreateGroupDTO createDto){
		groupService.createGroupRepository(auth.getName(), createDto);
		return ResponseEntity.ok().build();
	}
	/**
	 * 그룹 초대 코드 생성
	 * @param auth
	 * @param groupUuid
	 * @return
	 */
	@PostMapping("/v1/group/{groupUuid}/code")
	public ResponseEntity<String> createGroupInviteCode(Authentication auth, @PathVariable(name="groupUuid") String groupUuid){
		return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createInviteCode(auth.getName(),groupUuid));
	}
	/**
	 * 그룹 참여
	 * @param auth
	 * @param joinDto
	 * @return
	 */
	@PostMapping("/v1/group/join")
	public ResponseEntity<Void> joinGroupByInviteCode(Authentication auth ,@RequestBody @Valid JoinGroupDTO joinDto){
		groupService.joinGroup(auth.getName(),joinDto);
		return ResponseEntity.ok().build();
	}
	/**
	 * 그룹 탈퇴
	 * @param auth
	 * @param groupUuid
	 * @return
	 */
	@DeleteMapping("/v1/group/{groupUuid}")
	public ResponseEntity<Void> outGroup(Authentication auth, @PathVariable(name="groupUuid") String groupUuid){
		groupService.outGroup(auth.getName(), groupUuid);
		return ResponseEntity.ok().build();
	}
	/**
	 * 초대코드 미리 보기
	 * @param code
	 * @return
	 */
	@GetMapping("/v1/group/code/{code}/preview")
	public ResponseEntity<String> previewGroupName(@PathVariable(name="code")String code){
		return ResponseEntity.ok(groupService.previewGroupCode(code));
	}
}
