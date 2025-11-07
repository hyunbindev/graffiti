package com.hyunbindev.graffiti.constant.feed;

public enum FeedType {
	WHISPER("WHISPER");
	
	private final String type;
	
	FeedType(String type){
		this.type=type;
	}
	
	public String getFeedType() {
		return this.type;
	}
}