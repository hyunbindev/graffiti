package com.hyunbindev.graffiti.repository.redis.authentication;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hyunbindev.graffiti.entity.redis.authenticaiton.RefreshTokenEntity;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, String>{
	Optional<RefreshTokenEntity> findByUserUuid(String userUuid);
}
