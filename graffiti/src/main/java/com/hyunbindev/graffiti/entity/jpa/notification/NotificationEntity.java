package com.hyunbindev.graffiti.entity.jpa.notification;

import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
public class NotificationEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter
	private Long id;
	
	@Getter
	@ManyToOne(fetch = FetchType.LAZY)
	MemberEntity user;
	@Getter
	String message;
	
	@ManyToOne(fetch = FetchType.LAZY)
	FeedBaseEntity feed;
}
