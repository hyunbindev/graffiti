package com.hyunbindev.graffiti.controller.member;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyunbindev.graffiti.data.member.MemberInfoDTO;
import com.hyunbindev.graffiti.service.member.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class MemberController {
	private final MemberService memberService;
	/**
	 * 자기 정보 조회
	 * @param userUuid
	 * @return MemberInfo
	 */
	@GetMapping("/v1/member/me")
	public ResponseEntity<MemberInfoDTO> getMemberInfoSelf(Authentication auth){
		log.info(auth.getName());
		return ResponseEntity.ok(memberService.getMemberInfo(auth.getName()));
	}
	/**
	 * 타 회원 정보 조회
	 * @param targetUuid
	 * @return MemberInfo
	 */
	@GetMapping("/v1/member/{targetUuid}")
	public ResponseEntity<MemberInfoDTO> getMemberInfo(@PathVariable(name="targetUuid") String targetUuid){
		return ResponseEntity.ok(memberService.getMemberInfo(targetUuid));
	}
	/**
	 * 소속 그룹 조회
	 */
	@GetMapping("/v1/member/group/me")
	public ResponseEntity<?> getGroups(Authentication auth){
		return ResponseEntity.ok(memberService.getGroup(auth.getName()));
	}
}
