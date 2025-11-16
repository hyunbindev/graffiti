package com.hyunbindev.graffiti.repository.jpa;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.service.feed.FeedRankBatch;

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
			    (SELECT COUNT(*) FROM feed_comment_entity c WHERE c.feed_id = fbe.id) AS comment_count,
			    (SELECT COUNT(*) FROM feed_like_entity l WHERE l.feed_id = fbe.id) AS like_count,
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
			SELECT 
			    fbe.id,
			    fbe.type,
			    fbe.author_id,
			    fbe.created_at,
			    fbe.view_count,
			    fbe.deleted,
			    fbe.group_id,
			    (SELECT COUNT(*) FROM feed_comment_entity c WHERE c.feed_id = fbe.id) AS comment_count,
			    (SELECT COUNT(*) FROM feed_like_entity l WHERE l.feed_id = fbe.id) AS like_count,
			    se.answer,
			    se.hint,
			    COALESCE(se.text, we.text) AS text,
			    se.image_name,
			    we.image_name,
			    we.invisible_mention,
			    m.id AS member_id,
			    m.nick_name,
			    m.profile_img,
			    (
			        ((SELECT COUNT(*) FROM feed_comment_entity c WHERE c.feed_id = fbe.id) * 4 
			         + (SELECT COUNT(*) FROM feed_like_entity l WHERE l.feed_id = fbe.id) * 2 
			         + fbe.view_count)
			        / LOG((TIMESTAMPDIFF(SECOND, fbe.created_at, NOW()) / 36) + 2)
			    ) AS score
			FROM feed_base_entity fbe
			JOIN member_entity m ON m.id = fbe.author_id -- ⭐️ fbe.author_id로 수정
			LEFT JOIN secret_entity se ON fbe.id = se.id
			LEFT JOIN whisper_entity we ON fbe.id = we.id
			WHERE 
			    fbe.deleted = FALSE
			    AND fbe.group_id = :groupId
			ORDER BY score DESC
			LIMIT 10;
            """,
           nativeQuery = true)
	List<FeedBaseEntity> findByRankFeed(@Param("groupId") String groupId);
	
	/**
	 * 그룹 별 인기 게시글 산출 쿼리
	 * 덧글 점수 0.4
	 * 좋아요 점수 0.3
	 * 조회수 0.1
	 * 시간 감쇄 LOG10(현재 시간 - 작성날짜)
	 * @return
	 */
	@Query(value="""
			SELECT fbe.id AS feed_id, (COALESCE(c.comment_count, 0) * 0.4 + COALESCE(l.like_count, 0)* 0.3 + fbe.view_count * 0.1)/LOG10(10+ DATEDIFF(NOW(), fbe.created_at)) AS score
			FROM feed_base_entity AS fbe
			LEFT JOIN (SELECT feed_id, COUNT(*) AS comment_count FROM feed_comment_entity GROUP BY feed_id) c ON c.feed_id = fbe.id
			LEFT JOIN (SELECT feed_id, COUNT(*) AS like_count FROM feed_like_entity GROUP BY feed_id) l ON l.feed_id = fbe.id
			WHERE fbe.group_id = :groupId
			ORDER BY score DESC
			LIMIT :limit
			""",nativeQuery = true)
	List<FeedRankBatch.RankScoreDTO> getRankFeedByGroupId(@Param("groupId")String groupId, @Param("limit")int limit);
} 