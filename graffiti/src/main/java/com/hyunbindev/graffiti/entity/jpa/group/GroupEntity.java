package com.hyunbindev.graffiti.entity.jpa.group;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	
	@Getter
	@OneToMany(mappedBy = "group",fetch=FetchType.LAZY)
	private List<MemberGroupLinkedEntity> groupLinks = new ArrayList<>();
}