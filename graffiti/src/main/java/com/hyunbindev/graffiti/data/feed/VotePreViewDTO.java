package com.hyunbindev.graffiti.data.feed;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class VotePreViewDTO extends PostPreViewDTO{
	private String text;
}