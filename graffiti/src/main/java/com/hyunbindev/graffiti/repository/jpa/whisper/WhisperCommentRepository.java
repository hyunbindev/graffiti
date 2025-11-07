package com.hyunbindev.graffiti.repository.jpa.whisper;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperCommentEntity;

public interface WhisperCommentRepository extends JpaRepository<WhisperCommentEntity,Long>{

}
