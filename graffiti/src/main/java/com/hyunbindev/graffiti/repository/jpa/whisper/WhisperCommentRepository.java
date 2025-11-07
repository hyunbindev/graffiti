package com.hyunbindev.graffiti.repository.jpa.whisper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperCommentEntity;
import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity;

public interface WhisperCommentRepository extends JpaRepository<WhisperCommentEntity,Long>{

	@Query("SELECT c FROM WhisperCommentEntity c JOIN FETCH c.author WHERE c.whisper = :whisper")
	List<WhisperCommentEntity> findCommentsByWhisperWithAuthor(WhisperEntity whisper);

}
