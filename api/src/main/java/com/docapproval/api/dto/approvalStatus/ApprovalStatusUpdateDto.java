package com.docapproval.api.dto.approvalStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApprovalStatusUpdateDto {

    @Schema(description = "결재 신청 id")
    private Long approvalRequestId;

    @Schema(description = "결재자 id (사용자 id)")
    private Long userId;

    @Schema(description = "결재 여부 (승인 true, 반려 false)")
    private Boolean isApproved;

    @Schema(description = "결재 사유")
    private String reason;

}
