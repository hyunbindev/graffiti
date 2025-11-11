package com.hyunbindev.graffiti.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;

public interface FeedBaseRepository extends JpaRepository<FeedBaseEntity,Long> {
	@Query(value="""
			SELECT
				feed_base.type,
			    feed_base.id,
			    feed_base.group_id,
			    feed_base.deleted,
			    feed_base.created_at,
			    feed_base.author_id,
			    feed_base.view_count,
			    
			    whisper.*
			FROM (
			    SELECT id
			    FROM feed_base_entity AS base_sub
			    WHERE base_sub.group_id IN :groupIds AND base_sub.deleted = FALSE
			    ORDER BY base_sub.id DESC
			    LIMIT :limit OFFSET :offset
			) AS FEED
			JOIN feed_base_entity AS feed_base ON feed_base.id = FEED.id
			JOIN whisper_entity AS whisper ON whisper.id = FEED.id
			JOIN member_entity AS member ON member.id = feed_base.author_id
			LEFT JOIN whisper_entity_mention_members AS wmm ON wmm.whisper_entity_id = whisper.id
			LEFT JOIN member_entity AS mention_member ON mention_member.id = wmm.mention_members_id
			""",
			nativeQuery=true)
	List<FeedBaseEntity> findByGroupInAndDeletedFalse(@Param("groupIds")List<String> groupIds, @Param("limit")int limit, @Param("offset")int offset);
}