package com.hyunbindev.graffiti.data.feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FeedLikeDTO {
	private boolean isLike;
	private Long likeCount;
}
