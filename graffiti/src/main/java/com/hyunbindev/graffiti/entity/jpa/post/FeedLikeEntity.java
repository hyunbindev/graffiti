package com.hyunbindev.graffiti.entity.jpa.post;


import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.NoArgsConstructor;

/**
 * 게시글 좋아요 entity
 */
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "member_id"})})
public class FeedLikeEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	private FeedBaseEntity feed;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private MemberEntity liker;
	
	public FeedLikeEntity(FeedBaseEntity feed, MemberEntity liker) {
		this.feed=feed;
		this.liker=liker;
	}
}