package com.hyunbindev.graffiti.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hyunbindev.graffiti.entity.jpa.member.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, String>{
	Optional<MemberEntity> findByOauthKey(String ouathKey);
	
	//@Query("SELECT m From MemberEntity m JOIN FETCH m.groups WHERE m.id = :memberId")
	@Query("""
			SELECT m FROM MemberEntity m
			LEFT JOIN FETCH m.groupLinks link
			LEFT JOIN FETCH link.group
			WHERE m.id = :memberId
			""")
	Optional<MemberEntity> findById(@Param("memberId") String memberUuid);
}