package com.hyunbindev.graffiti.repository.jpa;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;
import com.hyunbindev.graffiti.service.feed.rank.FeedRankBatch;

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
	List<FeedBaseEntity> findByRankFeed(@Param("groupId") String groupId);
	
	/**
	 * 그룹 별 인기 게시글 산출 쿼리
	 * 덧글 점수 0.4
	 * 좋아요 점수 0.3
	 * 조회수 0.1
	 * 시간 감쇄 LOG10(현재 시간 - 작성날짜)
	 * @return
	 */
	List<FeedRankBatch.RankScoreDTO> getRankFeedByGroupId(@Param("groupId")String groupId, @Param("limit")int limit);
	
	
	
	List<FeedBaseEntity> findByGroupIdAndTextPrefix(@Param("groupId")String groupId, @Param("keyword")String keyWord ,@Param("lastId")Long lastId, @Param("size")Integer size);
} 