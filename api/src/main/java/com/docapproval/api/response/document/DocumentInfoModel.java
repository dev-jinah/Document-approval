package com.docapproval.api.response.document;

import com.docapproval.api.response.approvalRequest.ApprovalRequestModel;
import com.docapproval.api.response.approvalStatus.ApprovalStatusModel;
import com.docapproval.api.response.user.UserModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class DocumentInfoModel {
    @Schema(description = "결재신청 정보")
    private final ApprovalRequestModel approvalRequest;

    @Schema(description = "문서 정보")
    private final DocumentModel document;

    @Schema(description = "신청자 정보")
    private final UserModel creator;

    @Schema(description = "결재 상황 리스트")
    private final List<ApprovalStatusModel> approvalStatusList;

    public DocumentInfoModel(ApprovalRequestModel approvalRequest, DocumentModel document, UserModel creator, List<ApprovalStatusModel> approvalStatusList) {
        this.approvalRequest = approvalRequest;
        this.document = document;
        this.creator = creator;
        this.approvalStatusList = approvalStatusList;
    }
}
