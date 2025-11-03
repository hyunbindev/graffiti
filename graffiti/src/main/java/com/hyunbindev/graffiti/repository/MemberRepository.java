package com.hyunbindev.graffiti.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hyunbindev.graffiti.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, String>{
	Optional<MemberEntity> findByOauthKey(String ouathKey);
}