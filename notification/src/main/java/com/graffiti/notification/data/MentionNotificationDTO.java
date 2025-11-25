package com.graffiti.notification.data;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class MentionNotificationDTO implements Serializable{
	
	private static final long serialVersionUID = 853962263682477389L;

	@Getter
	private Long feedId;
	
	@Getter
	private MemberInfoDTO feedAuthor;
	
	@Getter
	private Boolean isInvisible;
	
	@Getter
	private List<String> receiverUuids;
}
