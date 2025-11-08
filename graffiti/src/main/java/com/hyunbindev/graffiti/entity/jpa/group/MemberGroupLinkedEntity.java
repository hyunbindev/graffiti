package com.hyunbindev.graffiti.entity.jpa.group;

import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 그룹 참여 중간 Entity
 * @author hyunbin DEv
 */
@Entity
@NoArgsConstructor
public class MemberGroupLinkedEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Getter
	@ManyToOne(fetch = FetchType.LAZY)
	private MemberEntity member;
	
	@Getter
	@ManyToOne(fetch = FetchType.LAZY)
	private GroupEntity group;
	
	public MemberGroupLinkedEntity(MemberEntity member, GroupEntity group) {
		this.member=member;
		this.group=group;
	}
}
