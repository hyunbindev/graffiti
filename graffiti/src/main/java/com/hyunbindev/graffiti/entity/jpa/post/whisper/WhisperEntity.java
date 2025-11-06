package com.hyunbindev.graffiti.entity.jpa.post.whisper;

import java.util.List;

import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;
import com.hyunbindev.graffiti.entity.jpa.post.PostBaseEntity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("whisper")
@SuperBuilder
@NoArgsConstructor
public class WhisperEntity extends PostBaseEntity{
	@Lob
	@Getter
	private String text;
	
	@Getter
	@OneToMany(fetch = FetchType.LAZY)
	private List<MemberEntity> mentionMembers;
	
	@Getter
	private boolean invisibleMention;
}