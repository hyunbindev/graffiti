package com.hyunbindev.graffiti.entity.jpa.member;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hyunbindev.graffiti.config.oauth.KakaoOauth2User;
import com.hyunbindev.graffiti.entity.jpa.group.GroupEntity;
import com.hyunbindev.graffiti.entity.jpa.group.MemberGroupLinkedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	@Getter
	private String id;
	
	@Column(unique = true)
	@Getter
	private String oauthKey;
	
	@Getter
	@OneToMany(mappedBy = "member",fetch=FetchType.LAZY)
	private List<MemberGroupLinkedEntity> groupLinks;
	
	@Column(unique = false, nullable = false)
	@Getter
	private String nickName;
	
	@Getter
	private String profileImg;
	
	@Getter
	private LocalDateTime lastLogin;
	
	public void updateMember(KakaoOauth2User user) {
		this.nickName = user.getNickName();
		this.profileImg = user.getProfileUrl();
		this.lastLogin = user.getConnectedTime();
	}
	/**
	 * 그룹 참여 여부 확인
	 * @param groupEntity
	 * @return
	 */
	public boolean isInGroup(GroupEntity groupEntity) {
		for(MemberGroupLinkedEntity link : this.groupLinks) {
			if(link.getGroup().equals(groupEntity)) return true;
		}
		return false;
	}
}