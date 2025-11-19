package com.hyunbindev.graffiti.repository.jpa.feed;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.service.feed.rank.FeedRankBatch;

import jakarta.persistence.LockModeType;

public interface FeedBaseRepository extends JpaRepository<FeedBaseEntity,Long> {
	/**
	 * 최신 게시글 조회
	 * @param groupIds
	 * @param size
	 * @param lastId
	 * @return
	 */
	@Query(value = """
			SELECT 
			    fbe.id,
			    fbe.type,
			    fbe.author_id,
			    fbe.created_at,
			    fbe.view_count,
			    fbe.deleted,
			    fbe.group_id,
			    fbe.comment_count,
			    fbe.like_count,
			    se.answer,
			    se.hint,
			    COALESCE(se.text, we.text) as text,
			    se.image_name,
			    we.image_name,
			    we.invisible_mention,
			    m.id AS member_id,
			    m.nick_name,
			    m.profile_img
			FROM
			    (SELECT 
			        id, author_id
			    FROM
			        feed_base_entity
			    WHERE
			        deleted = FALSE
					AND group_id IN :groupIds
					AND id < :lastId
			    ORDER BY id DESC
			    LIMIT :size) AS fbe_ids
			    JOIN
			    feed_base_entity fbe ON fbe.id = fbe_ids.id
			    JOIN
			    member_entity m ON m.id = fbe_ids.author_id
			    LEFT JOIN
			    secret_entity se ON fbe.id = se.id
			    LEFT JOIN
			    whisper_entity we ON fbe.id = we.id
            """,
           nativeQuery = true)
	List<FeedBaseEntity> findByDeletedIsFalseAndGroupIdInOrderByIdDesc(@Param("groupIds") List<String>groupIds, @Param("size")int size, @Param("lastId")Long lastId);
	
	/**
	 * 그룹별 인기 게시글 조회
	 * @param groupId
	 * @return
	 */
	@Query(value = """
		SELECT fbe.id, (
		    (COALESCE(fbe.comment_count, 0) * 1.5 + COALESCE(fbe.like_count, 0) * 2.0 + COALESCE(fbe.view_count, 0) * 0.5)
		    +
		    (10.0 / LOG2(TIMESTAMPDIFF(HOUR, fbe.created_at, NOW()) + 2.0))
		) AS rank_score
		FROM feed_base_entity fbe
		WHERE fbe.deleted = 0
		AND fbe.group_id = :groupId
		ORDER BY rank_score DESC, fbe.created_at DESC
		LIMIT 10;
		""", nativeQuery = true)
	List<Long> findByRankFeed(@Param("groupId") String groupId);
	
	@Query(value = """
			SELECT fbe.id, (
			    (COALESCE(fbe.comment_count, 0) * 1.5 + COALESCE(fbe.like_count, 0) * 2.0 + COALESCE(fbe.view_count, 0) * 0.5)
			    +
			    (10.0 / LOG2(TIMESTAMPDIFF(HOUR, fbe.created_at, NOW()) + 2.0))
			) AS rank_score
			FROM feed_base_entity fbe
			WHERE fbe.deleted = 0
			AND fbe.group_id = :groupId
			ORDER BY rank_score DESC, fbe.created_at DESC
			LIMIT :limit;
			""", nativeQuery = true)
	List<FeedRankBatch.RankScoreDTO> getRankFeedByGroupId(@Param("groupId")String groupId, @Param("limit")int limit);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT f FROM FeedBaseEntity f WHERE f.id = :id")
	Optional<FeedBaseEntity> findByIdForUpdate(@Param("id")Long id);
	
	@Query("UPDATE FeedBaseEntity f SET f.commentCount = f.commentCount+1 WHERE f.id = :id")
	void incrementCommentCount(@Param("id")Long id);
} 