package com.hyunbindev.graffiti.entity.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
public class GroupEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	@Getter
	String id;
	
	@Getter
	@Setter
	@NotBlank
	String name;
}