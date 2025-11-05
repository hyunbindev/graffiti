package com.hyunbindev.graffiti.data.group;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class JoinGroupDTO {
	@NotBlank(message = "초대 코드를 입력해 주세요.")
	private String code;
}
