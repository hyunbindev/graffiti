package com.hyunbindev.graffiti.controller.group;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyunbindev.graffiti.data.group.CreateGroupDTO;
import com.hyunbindev.graffiti.service.group.GroupService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class GroupController {
	private final GroupService groupService;
	
	@PostMapping("/v1/group")
	public ResponseEntity<Void> createGroup(Authentication auth, @RequestBody CreateGroupDTO createDto){
		groupService.createGroupRepository(auth.getName(), createDto);
		return ResponseEntity.ok().build();
	}
}
