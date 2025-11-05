package com.hyunbindev.graffiti.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hyunbindev.graffiti.entity.jpa.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, String>{
	Optional<MemberEntity> findByOauthKey(String ouathKey);
	
	@Query("SELECT m From MemberEntity m JOIN FETCH m.groups WHERE m.id = :memberId")
	Optional<MemberEntity> findByIdWithGroups(@Param("memberId") String id);
}