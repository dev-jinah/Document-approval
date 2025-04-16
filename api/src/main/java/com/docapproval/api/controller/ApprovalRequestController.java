package com.docapproval.api.controller;

import com.docapproval.api.dto.approvalRequest.ApprovalRequestCreateDto;
import com.docapproval.api.entity.ApprovalRequestEntity;
import com.docapproval.api.response.common.ErrorModel;
import com.docapproval.api.response.document.DocumentInfoModel;
import com.docapproval.api.service.ApprovalRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Tag(name = "ApprovalRequest", description = "결재 신청")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/approvals", produces = "application/json")
public class ApprovalRequestController {

    private final ApprovalRequestService approvalRequestService;

    @PostMapping("")
    @Operation(summary = "결재 신청 생성", description = "결재 신청 생성으로 문서 결재 신청",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "결재 신청 생성 데이터",
            content = @Content(schema = @Schema(implementation = ApprovalRequestCreateDto.class))
    ))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
    })
    public ResponseEntity submitApprovalRequest(
            @RequestBody @Validated ApprovalRequestCreateDto approvalRequestCreateDto) {

        ApprovalRequestEntity approvalRequest = approvalRequestService.submitApprovalRequest(approvalRequestCreateDto);
        if (null == approvalRequest) {
            return approvalRequestService.getErrorResponse();
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{approvalId:^[0-9]+$}")
    @Operation(summary = "결재 신청 정보 조회 By 결재신청 id", description = "결재신청 id로 결재 신청 정보 조회",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "결재신청 id", description = "결재신청 id", required = true)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = DocumentInfoModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
    })
    public ResponseEntity getApprovalRequest(
            @PathVariable(name = "approvalId") Long approvalId) {

        DocumentInfoModel approvalRequestModel = approvalRequestService.getApprovalRequestByApprovalId(approvalId);
        if (null == approvalRequestModel) {
            return approvalRequestService.getErrorResponse();
        }

        return ResponseEntity.ok(approvalRequestModel);
    }
}
