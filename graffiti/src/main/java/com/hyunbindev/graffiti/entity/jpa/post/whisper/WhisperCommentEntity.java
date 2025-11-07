package com.hyunbindev.graffiti.entity.jpa.post.whisper;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Whisper Comment Entity
 * @author hyunbinDev
 */
@Entity
@Builder
public class WhisperCommentEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Getter
	private Long id;
	
	//작성자
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@Getter
	private MemberEntity author;
	
	//부모 게시글
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private WhisperEntity whisper;
	
	//작성 내용
	@Lob
	@Getter
	@NotBlank
	private String text;
	
	//생성날자
	@CreationTimestamp
	@Getter
	private LocalDateTime createdAt;
	
	@Getter
	@Setter
	private boolean deleted;
}
