package com.hyunbindev.graffiti.service.feed.like;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedLikeCountService {
	@Async
	@Transactional
	public void incrementFeedLikeCount(FeedBaseEntity entity) {
		entity.increCommentCount();
	}
	
	@Async
	@Transactional
	public void decrementFeedLikeCount(FeedBaseEntity entity) {
		entity.decreCommentCount();
	}
}
