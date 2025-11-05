package com.hyunbindev.graffiti.entity.jpa;

import java.time.LocalDateTime;
import java.util.List;

import com.hyunbindev.graffiti.config.oauth.KakaoOauth2User;

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
	@OneToMany(fetch=FetchType.LAZY)
	private List<GroupEntity> groups;
	
	@Column(unique = true, nullable = false)
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
}