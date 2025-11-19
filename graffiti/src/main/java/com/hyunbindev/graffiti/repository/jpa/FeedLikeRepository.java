package com.hyunbindev.graffiti.repository.jpa;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedLikeEntity;

public interface FeedLikeRepository extends JpaRepository<FeedLikeEntity,Long>{
	
	Optional<FeedLikeEntity> findByFeedAndLiker(FeedBaseEntity feed, MemberEntity user);
	
	long countByFeed(FeedBaseEntity feed);
	
	@Query("SELECT COUNT(fl) > 0 FROM FeedLikeEntity fl WHERE fl.feed.id = :feedId AND fl.liker.id = :likerId")	
	boolean existsByFeedIdAndLikerId(@Param("feedId")Long feedId, @Param("likerId")String likerId);

}