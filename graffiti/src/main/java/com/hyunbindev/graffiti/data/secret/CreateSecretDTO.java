package com.hyunbindev.graffiti.data.secret;

import lombok.Getter;

@Getter
public class CreateSecretDTO {
	private String groupUuid;
	private String text;
	private String hint;
	private String answer;
}
