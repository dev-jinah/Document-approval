package com.docapproval.api.dto.approvalRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApprovalStepDto {

    @Schema(description = "결재 단계")
    private Long step;

    @Schema(description = "결재자 아이디 (사용자 id)")
    private Long userId;

    @Schema(description = "결자자 이름 (사용자 명)")
    private String userName;
}
