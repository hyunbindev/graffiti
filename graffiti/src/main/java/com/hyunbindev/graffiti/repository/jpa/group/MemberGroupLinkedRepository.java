package com.hyunbindev.graffiti.repository.jpa.group;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hyunbindev.graffiti.entity.jpa.group.GroupEntity;
import com.hyunbindev.graffiti.entity.jpa.group.MemberGroupLinkedEntity;

public interface MemberGroupLinkedRepository extends JpaRepository<MemberGroupLinkedEntity,Long>{
	
	@Query("SELECT gl FROM MemberGroupLinkedEntity gl JOIN FETCH gl.member WHERE gl.group = :group AND gl.member.nickName LIKE :keyWord")
	List<MemberGroupLinkedEntity> findByGroupFetchJoin(@Param("group")GroupEntity group, @Param("keyWord")String keyWord);
}
