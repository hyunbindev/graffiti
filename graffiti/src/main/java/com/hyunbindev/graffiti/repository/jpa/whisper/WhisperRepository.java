package com.hyunbindev.graffiti.repository.jpa.whisper;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hyunbindev.graffiti.entity.jpa.post.whisper.WhisperEntity;

public interface WhisperRepository extends JpaRepository<WhisperEntity,Long>{
	
	@Query("SELECT w From WhisperEntity w JOIN FETCH w.author WHERE w.id = :id")
	Optional<WhisperEntity> findByIdWithAuthor(@Param("id")Long id);
}
