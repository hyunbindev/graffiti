package com.hyunbindev.graffiti.entity.jpa.post;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.hyunbindev.graffiti.entity.jpa.group.GroupEntity;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
/**
 * 게시글 공통 base Entity
 * @author hyunbinDev
 */
@Entity
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="type")
@NoArgsConstructor
public class PostBaseEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Getter
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@Getter
	private MemberEntity author;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@Getter
	private GroupEntity group;
	
	@CreationTimestamp
	@Getter
	private LocalDateTime createdAt;
}