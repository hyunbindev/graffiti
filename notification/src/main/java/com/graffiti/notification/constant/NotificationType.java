package com.graffiti.notification.constant;

public enum NotificationType {
	COMMENT("COMMENT"),
	MENTION("MENTION");
	
	public final String type;
	
	NotificationType(String type){
		this.type=type;
	}
}
