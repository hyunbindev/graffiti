package com.hyunbindev.graffiti.data.feed;

import com.hyunbindev.graffiti.constant.feed.FeedType;
import com.hyunbindev.graffiti.data.member.MemberInfoDTO;
import com.hyunbindev.graffiti.entity.jpa.post.secret.SecretEntity;
import com.hyunbindev.graffiti.service.secret.SecretService;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuperBuilder
@Getter
@Slf4j
public class SecretPreViewDTO extends PostPreViewDTO{
	String hint;
	public static SecretPreViewDTO mappingDTO(SecretEntity entity, boolean isLike) {
		String preViewText = SecretService.randomText(entity.getText()).substring(0, Math.min(entity.getText().length(), 50));
		return SecretPreViewDTO.builder()
				.authorInfo(new MemberInfoDTO(entity.getAuthor()))
				.id(entity.getId())
				.type(FeedType.SECRET)
				.createdAt(entity.getCreatedAt())
				.previewText(preViewText)
				.commentCount(entity.getCommentCount())
				.likeCount(entity.getLikeCount())
				.isLiked(isLike)
				.build();
	}
}
