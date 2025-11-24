package com.graffiti.notification.data;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class MentionNotificationDTO {
	@Getter
	private Long feedId;
	
	@Getter
	private MemberInfoDTO feedAuthor;
	
	@Getter
	private boolean isInvisible;
	
	@Getter
	private List<String> receiverUuids;
}
