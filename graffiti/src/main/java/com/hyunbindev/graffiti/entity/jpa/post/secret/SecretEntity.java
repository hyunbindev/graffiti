package com.hyunbindev.graffiti.entity.jpa.post.secret;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("secret")
@SuperBuilder
@NoArgsConstructor
public class SecretEntity {
	@Lob
	@Getter
	private String text;
	
	@Getter
	private String imageName;
	
	@Getter
	private String hint;
	
	@Getter
	private String answer;
}