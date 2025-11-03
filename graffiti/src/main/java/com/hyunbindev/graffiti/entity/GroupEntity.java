package com.hyunbindev.graffiti.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
public class GroupEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	String id;
	
	@Getter
	String name;
}