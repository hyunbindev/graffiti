package com.hyunbindev.graffiti.data.feed;

import java.time.LocalDateTime;

import com.hyunbindev.graffiti.constant.feed.FeedType;
import com.hyunbindev.graffiti.data.member.MemberInfoDTO;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class PostPreViewDTO {
	private FeedType type;
	private Long id;
	private MemberInfoDTO authorInfo;
	private LocalDateTime createdAt;
	private String previewText;
	private Long viewCount;
	private Long commentCount;
	private Long likeCount;
	private boolean isBlinded;
}