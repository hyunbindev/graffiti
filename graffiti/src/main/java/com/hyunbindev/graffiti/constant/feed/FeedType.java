package com.hyunbindev.graffiti.constant.feed;
/**
 * 게시글 타입 enum
 */
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