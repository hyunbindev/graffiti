package com.hyunbindev.graffiti.data.group;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateGroupDTO {
	@NotBlank(message = "그룹이름은 필수 항목 입니다.")
	private String groupName;
}
