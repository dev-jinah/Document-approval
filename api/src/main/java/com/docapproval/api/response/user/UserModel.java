package com.docapproval.api.response.user;

import com.docapproval.api.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserModel {

    @Schema(description = "사용자 id")
    private final Long id;

    @Schema(description = "사용자의 이름 또는 닉네임, 사용자의 고유한 아이디 (중복 불가)")
    private final String name;

    public UserModel(UserEntity user) {
        this.id = user.getId();
        this.name = user.getName();
    }
}
