package com.hyunbindev.graffiti.entity.jpa.post.secret;

import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("secret")
@SuperBuilder
@NoArgsConstructor
public class SecretEntity extends FeedBaseEntity{
	@Lob
	@Getter
	@Setter
	private String text;
	
	@Getter
	private String imageName;
	
	@Getter
	private String hint;
	
	@Getter
	private String answer;
}