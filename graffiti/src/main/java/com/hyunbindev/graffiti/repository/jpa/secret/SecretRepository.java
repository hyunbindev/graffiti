package com.hyunbindev.graffiti.repository.jpa.secret;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hyunbindev.graffiti.entity.jpa.post.secret.SecretEntity;

public interface SecretRepository extends JpaRepository<SecretEntity,Long>{

}
