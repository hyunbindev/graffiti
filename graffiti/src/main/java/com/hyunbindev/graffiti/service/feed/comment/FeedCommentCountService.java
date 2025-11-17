package com.hyunbindev.graffiti.service.feed.comment;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyunbindev.graffiti.entity.jpa.post.FeedBaseEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedCommentCountService {
	@Async
	@Transactional
	public void incrementFeedCommentCount(FeedBaseEntity entity) {
		entity.increCommentCount();
	}
	
	@Async
	@Transactional
	public void decrementFeedCommentCount(FeedBaseEntity entity) {
		entity.decreCommentCount();
	}
	
}
