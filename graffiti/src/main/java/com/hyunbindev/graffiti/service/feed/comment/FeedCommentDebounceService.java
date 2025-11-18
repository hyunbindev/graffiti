package com.hyunbindev.graffiti.service.feed.comment;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FeedCommentDebounceService {
	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
}
