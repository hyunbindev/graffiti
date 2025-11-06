package com.hyunbindev.graffiti.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hyunbindev.graffiti.entity.jpa.group.GroupEntity;
import com.hyunbindev.graffiti.entity.jpa.post.PostBaseEntity;

public interface PostBaseRepository extends JpaRepository<PostBaseEntity,Long>{
	
	List<PostBaseEntity> findByGroupIn(List<GroupEntity> groups, Pageable pageable);
}