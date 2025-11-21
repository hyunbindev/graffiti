package com.hyunbindev.graffiti.repository.jpa.feed;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedCommentEntity;

public interface FeedCommentRepository extends JpaRepository<FeedCommentEntity,Long>{
	@Query(value="""
		    SELECT 
		        c.id, c.feed_id, c.text, c.created_at, c.author_id, c.deleted,
		        m.id AS member_id,
		        m.nick_name, m.profile_img
		    FROM feed_comment_entity c
		    JOIN member_entity m ON m.id = c.author_id
		    WHERE c.feed_id = :feedId AND c.id < :lastId
		    AND c.deleted = false
		    ORDER BY c.id DESC
		    LIMIT :size
		    """, 
		    nativeQuery=true)
	List<FeedCommentEntity> findCommentsByFeedWithAuthor(@Param("feedId")Long feed, @Param("lastId")Long lastId, @Param("size")Integer size);
	
	long countByFeed(FeedBaseEntity feed);
}
