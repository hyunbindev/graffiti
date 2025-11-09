package com.hyunbindev.graffiti.repository.redis.post;

import org.springframework.data.repository.CrudRepository;

import com.hyunbindev.graffiti.entity.redis.post.FeedViewEntity;

public interface FeedViewRepository extends CrudRepository<FeedViewEntity, Long>{
	
}