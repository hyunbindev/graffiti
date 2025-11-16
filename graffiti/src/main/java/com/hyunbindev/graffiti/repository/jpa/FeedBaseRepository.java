package com.hyunbindev.graffiti.repository.jpa;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;

public interface FeedBaseRepository extends JpaRepository<FeedBaseEntity,Long> {
	
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
			        -- ⭐️ c.comment_count, l.like_count 대신 스칼라 서브쿼리를 직접 사용해야 함
			        ((SELECT COUNT(*) FROM feed_comment_entity c WHERE c.feed_id = fbe.id) * 4 
			         + (SELECT COUNT(*) FROM feed_like_entity l WHERE l.feed_id = fbe.id) * 2 
			         + fbe.view_count)
			        / LOG((TIMESTAMPDIFF(SECOND, fbe.created_at, NOW()) / 36) + 2)
			    ) AS score
			FROM feed_base_entity fbe
			-- ⭐️ fbe_ids 서브쿼리가 없으므로, fbe를 직접 JOIN합니다.
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
} 