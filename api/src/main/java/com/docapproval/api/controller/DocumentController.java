package com.docapproval.api.controller;

import com.docapproval.api.response.common.ErrorModel;
import com.docapproval.api.response.document.DocumentInfoModel;
import com.docapproval.api.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Document", description = "문서")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/document", produces = "application/json")
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("")
    @Operation(summary = "문서 정보 전체 조회", description = "문서 정보 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentInfoModel.class)))),
    })
    public ResponseEntity getAllDocumentInfo() {

        Set<DocumentInfoModel> documentInfoModels = documentService.getAllDocumentInfo();
        if (null == documentInfoModels) {
            return documentService.getErrorResponse();
        }

        return ResponseEntity.ok(documentInfoModels);
    }

    @GetMapping("{documentId:^[0-9]+$}")
    @Operation(summary = "문서 정보 조회 By 문서 id", description = "문서 id로 결재 신청 정보 조회",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "문서 id", description = "문서 id", required = true)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = DocumentInfoModel.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
    })
    public ResponseEntity getApprovalRequest(
            @PathVariable(name = "documentId") Long documentId) {

        DocumentInfoModel documentInfoModel = documentService.getDocumentByDocumentId(documentId);
        if (null == documentInfoModel) {
            return documentService.getErrorResponse();
        }

        return ResponseEntity.ok(documentInfoModel);
    }

    @GetMapping("{userId:^[0-9]+$}/outbox")
    @Operation(summary = "내가 생성한 문서 중 결재 진행 중인 문서 조회 (OUTBOX) by 사용자 Id", description = "사용자 id로 내가 생성한 문서 중 결재 진행 중인 문서 조회 (OUTBOX)",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "사용자(신청자) id", description = "사용자(신청자) id", required = true)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentInfoModel.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
    })
    public ResponseEntity getMyOngoingDocuments(
            @PathVariable(name = "userId") Long userId) {

        Set<DocumentInfoModel> documents = documentService.getMyOngoingDocumentsByUserId(userId);
        if (null == documents) {
            return documentService.getErrorResponse();
        }

        return ResponseEntity.ok(documents);
    }

    @GetMapping("{userName:^.*\\D+.*$}/outbox")
    @Operation(summary = "내가 생성한 문서 중 결재 진행 중인 문서 조회 (OUTBOX) by 사용자 name", description = "사용자 name으로 내가 생성한 문서 중 결재 진행 중인 문서 조회 (OUTBOX)",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "사용자(신청자) name", description = "사용자(신청자) name", required = true)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentInfoModel.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
    })
    public ResponseEntity getMyOngoingDocuments(
            @PathVariable(name = "userName") String userName) {

        Set<DocumentInfoModel> documents = documentService.getMyOngoingDocumentsByUserName(userName);
        if (null == documents) {
            return documentService.getErrorResponse();
        }

        return ResponseEntity.ok(documents);
    }

    @GetMapping("{userId:^[0-9]+$}/inbox")
    @Operation(summary = "내가 결재를 해야 할 문서 조회 (INBOX) by 사용자 id", description = "사용자 id로 내가 결재를 해야 할 문서 조회 (INBOX)",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "사용자(결재자) id", description = "사용자(결재자) id", required = true)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentInfoModel.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
    })
    public ResponseEntity getDocumentsForApproval(
            @PathVariable(name = "userId") Long userId) {

        Set<DocumentInfoModel> documents = documentService.getDocumentsForApprovalByUserId(userId);
        if (null == documents) {
            return documentService.getErrorResponse();
        }

        return ResponseEntity.ok(documents);
    }

    @GetMapping("{userName:^.*\\D+.*$}/inbox")
    @Operation(summary = "내가 결재를 해야 할 문서 조회 (INBOX) by 사용자 name", description = "사용자 name으로 내가 결재를 해야 할 문서 조회 (INBOX)",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "사용자(결재자) name", description = "사용자(결재자) name", required = true)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentInfoModel.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
    })
    public ResponseEntity getDocumentsForApproval(
            @PathVariable(name = "userName") String userName) {

        Set<DocumentInfoModel> documents = documentService.getDocumentsForApprovalByUserName(userName);
        if (null == documents) {
            return documentService.getErrorResponse();
        }

        return ResponseEntity.ok(documents);
    }

    @GetMapping("{userId:^[0-9]+$}/archive")
    @Operation(summary = "내가 관여한 문서 중 결재가 완료(승인 또는 거절)된 문서 조회 (ARCHIVE) by 사용자 id", description = "사용자 id로 내가 관여한 문서 중 결재가 완료(승인 또는 거절)된 문서 조회 (ARCHIVE)",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "사용자(신청자, 결재자) id", description = "사용자(신청자, 결재자) id", required = true)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentInfoModel.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
    })
    public ResponseEntity getCompletedApprovalDocuments(
            @PathVariable(name = "userId") Long userId) {

        Set<DocumentInfoModel> documents = documentService.getCompletedApprovalDocumentsByUserId(userId);
        if (null == documents) {
            return documentService.getErrorResponse();
        }

        return ResponseEntity.ok(documents);
    }

    @GetMapping("{userName:^.*\\D+.*$}/archive")
    @Operation(summary = "내가 관여한 문서 중 결재가 완료(승인 또는 거절)된 문서 조회 (ARCHIVE) by 사용자 name", description = "사용자 name로 내가 관여한 문서 중 결재가 완료(승인 또는 거절)된 문서 조회 (ARCHIVE)",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "사용자(신청자, 결재자) name", description = "사용자(신청자, 결재자) name", required = true)
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentInfoModel.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorModel.class))),
    })
    public ResponseEntity getCompletedApprovalDocuments(
            @PathVariable(name = "userName") String userName) {

        Set<DocumentInfoModel> documents = documentService.getCompletedApprovalDocumentsByUserName(userName);
        if (null == documents) {
            return documentService.getErrorResponse();
        }

        return ResponseEntity.ok(documents);
    }
}
