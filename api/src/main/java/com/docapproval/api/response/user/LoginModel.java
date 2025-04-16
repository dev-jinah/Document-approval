package com.docapproval.api.response.user;

import com.docapproval.api.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class LoginModel {

    @Schema(description = "사용자 id")
    private final Long id;

    @Schema(description = "사용자의 이름 또는 닉네임, 사용자의 고유한 아이디 (중복 불가)")
    private final String name;

    @Schema(description = "사용자 생성 여부 (생성된 사용자 true, 기존 사용자 false)")
    private final Boolean isCreated;

    @Schema(description = "사용자 토큰")
    private final String token;

    public LoginModel(UserEntity user, Boolean isCreated, String token) {
        this.id = user.getId();
        this.name = user.getName();
        this.isCreated = isCreated;
        this.token = token;
    }
}
