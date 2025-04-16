package com.docapproval.api.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginCreateDto {

    @Schema(description = "사용자의 이름 또는 닉네임, 사용자의 고유한 아이디 (중복 불가)")
    private String name;

    @Schema(description = "비밀번호")
    private String password;
}
