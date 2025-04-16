package com.docapproval.api.response.approvalStatus;

import com.docapproval.api.entity.ApprovalStatusEntity;
import com.docapproval.api.response.user.UserModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApprovalStatusModel {

    @Schema(description = "결재자 정보")
    private final UserModel approvalUser;

    @Schema(description = "결재 단계")
    private final Long step;

    @Schema(description = "결재 사유")
    private final String reason;

    @Schema(description = "결재 상태 (승인(2), 반려(3))")
    private final Long status;

    private String statusName;

    @Schema(description = "결재 일시")
    private final LocalDateTime updateTime;


    public ApprovalStatusModel(ApprovalStatusEntity approvalStatus, UserModel approvalUser) {
        this.step = approvalStatus.getStep();
        this.reason = approvalStatus.getReason();
        this.status = approvalStatus.getStatus();
        if(approvalStatus.getStatus() != null && approvalStatus.getStatus() == 2) {
            this.statusName = "승인";
        } else if(approvalStatus.getStatus() != null && approvalStatus.getStatus() == 3) {
            this.statusName = "반려";
        }
        this.updateTime = approvalStatus.getUpdateTime();

        this.approvalUser = approvalUser;
    }
}
