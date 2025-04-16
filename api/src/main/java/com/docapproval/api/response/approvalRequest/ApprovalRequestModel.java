package com.docapproval.api.response.approvalRequest;

import com.docapproval.api.entity.ApprovalRequestEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ApprovalRequestModel {

    @Schema(description = "결재신청 Id")
    private final Long id;

    @Schema(description = "결재신청 상태 (대기(0), 진행(1), 승인(2), 반려(3))")
    private final Long status;

    private String statusName;

    public ApprovalRequestModel(ApprovalRequestEntity approvalRequest) {
        this.id = approvalRequest.getId();
        this.status = approvalRequest.getStatus();

        if(approvalRequest.getStatus() == 0) {
            this.statusName = "대기";
        } else if(approvalRequest.getStatus() == 1) {
            this.statusName = "진행";
        } else if(approvalRequest.getStatus() == 2) {
            this.statusName = "승인";
        } else if(approvalRequest.getStatus() == 3) {
            this.statusName = "반려";
        }
    }
}
