package com.hyunbindev.graffiti.entity.jpa.post;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.hyunbindev.graffiti.entity.jpa.group.GroupEntity;
import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
/**
 * 게시글 공통 base Entity
 * @author hyunbinDev
 */
@Entity
@Table(indexes = {@Index(name = "idx_feed_sort_optimized", columnList = "group_id, deleted, id DESC") })
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="type")
@NoArgsConstructor
public class  FeedBaseEntity{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter
	private Long id;
	
	//작성자 필드
	@ManyToOne(fetch = FetchType.LAZY)
	@Getter
	@NotNull
	private MemberEntity author;
	
	//확인 가능한 그룹 필드
	@ManyToOne(fetch = FetchType.LAZY)
	@Getter
	@NotNull
	private GroupEntity group;
	
	//생성 날자
	@CreationTimestamp
	@Getter
	private LocalDateTime createdAt;
	
	@Getter
	@Setter
	long viewCount;
	
	@Getter
	long commentCount;
	
	@Getter
	long likeCount;
	
	//Soft delete 필드
	@Getter
	@Setter
	private boolean deleted;
	
	public void increViewCount(long viewCount) {
		this.viewCount+=viewCount;
	}
	
	public void increCommentCount() {
		this.commentCount++;
	}
	
	public void decreCommentCount() {
		this.commentCount--;
	}
	
	public void increLikeCount() {
		this.likeCount++;
	}
	
	public void decreLikeCount() {
		this.likeCount--;
	}
}