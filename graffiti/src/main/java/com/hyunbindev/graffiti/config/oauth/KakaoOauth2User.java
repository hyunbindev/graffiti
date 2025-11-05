package com.hyunbindev.graffiti.config.oauth;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.hyunbindev.graffiti.constant.Role;

import lombok.Getter;
import lombok.Setter;

public class KakaoOauth2User implements OAuth2User{
	
	private final String name;
	@Getter
	private final String profileUrl;
	@Getter
	private final String nickName;
	private Collection<? extends GrantedAuthority> authorities;
	private final Map<String,Object> attributes;
	@Getter
	private final LocalDateTime connectedTime;
	
	@Getter
	@Setter
	private String memberUuid;
	
	@SuppressWarnings("unchecked")
	public KakaoOauth2User(OAuth2User user) {
		Map<String,Object> attributes = user.getAttributes();
		Map<String,Object> properties = (Map<String,Object>)attributes.get("properties");
		
		this.attributes = attributes;
		this.name = String.valueOf(attributes.get("id"));
		this.nickName = (String)properties.get("nickname");
		this.profileUrl = (String)properties.get("profile_image");
		Instant instant = Instant.parse((String)attributes.get("connected_at"));
		this.connectedTime = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
		this.authorities = List.of(new SimpleGrantedAuthority(Role.GUEST.getKey()));
	}
	
	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public void setAuthority(Role role) {
		this.authorities = List.of(new SimpleGrantedAuthority(role.getKey()));
	}
}
