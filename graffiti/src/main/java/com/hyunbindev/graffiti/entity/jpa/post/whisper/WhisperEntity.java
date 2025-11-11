package com.hyunbindev.graffiti.entity.jpa.post.whisper;

import java.util.List;

import org.hibernate.annotations.BatchSize;

import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("whisper")
@SuperBuilder
@NoArgsConstructor
public class WhisperEntity extends FeedBaseEntity{
	@Lob
	@Getter
	private String text;
	
	@Getter
	@ManyToMany(fetch = FetchType.LAZY)
	@BatchSize(size = 20)
	private List<MemberEntity> mentionMembers;
	
	@Getter
	private String imageName;
	
	@Getter
	private boolean invisibleMention;
}