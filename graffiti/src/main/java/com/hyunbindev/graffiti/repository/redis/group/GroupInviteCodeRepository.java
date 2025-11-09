package com.hyunbindev.graffiti.repository.redis.group;

import org.springframework.data.repository.CrudRepository;

import com.hyunbindev.graffiti.entity.redis.group.GroupInviteCodeEntity;

public interface GroupInviteCodeRepository extends CrudRepository<GroupInviteCodeEntity, String>{

}
