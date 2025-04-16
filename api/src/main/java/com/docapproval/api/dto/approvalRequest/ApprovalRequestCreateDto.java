package com.docapproval.api.dto.approvalRequest;

import com.docapproval.api.dto.document.DocumentCreateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApprovalRequestCreateDto {

    @Schema(description = "문서 생성")
    private DocumentCreateDto document;

    @Schema(description = "신청자 생성")
    private CreatorDto creator;

    @Schema(description = "결재 단계 생성")
    private List<ApprovalStepDto> approvalStepList;

}
