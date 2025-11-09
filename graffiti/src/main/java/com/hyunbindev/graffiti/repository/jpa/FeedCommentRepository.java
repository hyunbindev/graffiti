package com.hyunbindev.graffiti.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedCommentEntity;

public interface FeedCommentRepository extends JpaRepository<FeedCommentEntity,Long>{
	
	@Query("SELECT c FROM FeedCommentEntity c JOIN FETCH c.author WHERE c.feed = :feed")
	List<FeedCommentEntity> findCommentsByFeedWithAuthor(@Param("feed")FeedBaseEntity feed);
	
	long countByFeed(FeedBaseEntity feed);
}
