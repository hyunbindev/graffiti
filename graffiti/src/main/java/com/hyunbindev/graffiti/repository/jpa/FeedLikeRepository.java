package com.hyunbindev.graffiti.repository.jpa;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedLikeEntity;

public interface FeedLikeRepository extends JpaRepository<FeedLikeEntity,Long>{
	
	Optional<FeedLikeEntity> findByFeedAndLiker(FeedBaseEntity feed, MemberEntity user);
	
	long countByFeed(FeedBaseEntity feed);
}