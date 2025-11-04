package com.hyunbindev.graffiti.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hyunbindev.graffiti.entity.GroupEntity;

public interface GroupRepository extends JpaRepository<GroupEntity, String>{

}
