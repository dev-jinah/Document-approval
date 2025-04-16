package com.docapproval.api.controller;

import com.docapproval.api.dto.approvalRequest.ApprovalRequestCreateDto;
import com.docapproval.api.dto.approvalStatus.ApprovalStatusUpdateDto;
import com.docapproval.api.entity.ApprovalStatusEntity;
import com.docapproval.api.response.common.ErrorModel;
import com.docapproval.api.service.ApprovalStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ApprovalStatus", description = "결재 상황 (결재 처리)")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/approvalstatus", produces = "application/json")
public class ApprovalStatusController {

    private final ApprovalStatusService approvalStatusService;

    @PutMapping("")
    @Operation(summary = "결재 처리", description = "문서 결재 신청의 결재 처리",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "결재 처리 데이터",
                    content = @Content(schema = @Schema(implementation = ApprovalStatusUpdateDto.class))
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
    })
    public ResponseEntity updateApprovalStatus(
            @RequestBody @Validated ApprovalStatusUpdateDto approvalStatusUpdateDto) {

        ApprovalStatusEntity approvalStatus = approvalStatusService.updateApprovalStatus(approvalStatusUpdateDto);
        if (null == approvalStatus) {
            return approvalStatusService.getErrorResponse();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
