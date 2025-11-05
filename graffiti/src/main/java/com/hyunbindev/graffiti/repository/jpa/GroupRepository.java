package com.hyunbindev.graffiti.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hyunbindev.graffiti.entity.jpa.GroupEntity;

public interface GroupRepository extends JpaRepository<GroupEntity, String>{

}
