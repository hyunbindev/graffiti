package com.hyunbindev.graffiti.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hyunbindev.graffiti.entity.jpa.group.GroupEntity;
import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;

public interface FeedBaseRepository extends JpaRepository<FeedBaseEntity,Long>{
	List<FeedBaseEntity> findByGroupInAndDeletedFalse(List<GroupEntity> groups, Pageable pageable);
}