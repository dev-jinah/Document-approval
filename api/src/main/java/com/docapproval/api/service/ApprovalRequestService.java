package com.docapproval.api.service;

import com.docapproval.api.dto.approvalRequest.ApprovalRequestCreateDto;
import com.docapproval.api.dto.approvalRequest.ApprovalStepDto;
import com.docapproval.api.dto.approvalRequest.CreatorDto;
import com.docapproval.api.dto.document.DocumentCreateDto;
import com.docapproval.api.entity.ApprovalRequestEntity;
import com.docapproval.api.entity.ApprovalStatusEntity;
import com.docapproval.api.entity.DocumentEntity;
import com.docapproval.api.entity.UserEntity;
import com.docapproval.api.repository.ApprovalRequestRepository;
import com.docapproval.api.response.document.DocumentInfoModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApprovalRequestService extends BaseService {

    private final DocumentService documentService;
    private final UserService userService;
    private final ApprovalStatusService approvalStatusService;
    private final  ApprovalRequestRepository approvalRequestRepository;

    public ApprovalRequestService(DocumentService documentService, UserService userService, ApprovalRequestRepository approvalRequestRepository, ApprovalStatusService approvalStatusService) {
        this.documentService = documentService;
        this.userService = userService;
        this.approvalRequestRepository = approvalRequestRepository;
        this.approvalStatusService = approvalStatusService;
    }

    @Transactional
    public ApprovalRequestEntity submitApprovalRequest(ApprovalRequestCreateDto approvalRequestCreateDto) {
        if(approvalRequestCreateDto.getDocument() == null) {
            setErrorResponse(HttpStatus.BAD_REQUEST, "문서를 확인해주세요.");
            return null;
        }
        DocumentCreateDto documentCreateDto = approvalRequestCreateDto.getDocument();
        if(documentCreateDto.getTitle() == null || documentCreateDto.getTitle().trim().length() == 0) {
            setErrorResponse(HttpStatus.BAD_REQUEST, "문서명을 입력해주세요.");
            return null;
        }
        if(documentCreateDto.getCategory() == null || documentCreateDto.getCategory().trim().length() == 0) {
            setErrorResponse(HttpStatus.BAD_REQUEST, "분류를 입력해주세요.");
            return null;
        }
        if(documentCreateDto.getContent() == null || documentCreateDto.getContent().trim().length() == 0) {
            setErrorResponse(HttpStatus.BAD_REQUEST, "내용를 입력해주세요.");
            return null;
        }
        //신청자
        CreatorDto creatorDto = approvalRequestCreateDto.getCreator();

        if(creatorDto.getId() == null || (creatorDto.getName() == null || creatorDto.getName().trim().length() == 0)) {
            setErrorResponse(HttpStatus.BAD_REQUEST, "신청자를 확인해주세요.");
            return null;
        }
        // 결재자 확인
        List<ApprovalStepDto> approvalStepList = approvalRequestCreateDto.getApprovalStepList();
        if(approvalStepList == null || approvalStepList.size() == 0) {
            setErrorResponse(HttpStatus.BAD_REQUEST, "결재자를 확인해주세요.");
            return null;
        }
        // 문서 저장
        DocumentEntity document = documentService.createDocument(approvalRequestCreateDto.getDocument());
        if(document == null) {
            setErrorResponse(documentService.getStatusCode(), documentService.getErrorMessage());
            return null;
        }
        // 신청자
        UserEntity creator = userService.getUserInfo(creatorDto);
        if (creator == null) {
            setErrorResponse(HttpStatus.NOT_FOUND, "신청자 정보가 존재하지 않습니다.");
            return null;
        }
        // 결재 신청 저장
        ApprovalRequestEntity approvalRequest = createApprovalRequest(document, creator);
        if(approvalRequest == null) {
            return null;
        }
        // 결재 상황 저장
        List<ApprovalStatusEntity> approvalStatus = approvalStatusService.createApprovalStatusEntity(approvalRequest.getId(), approvalRequestCreateDto.getApprovalStepList());
        if(approvalStatus == null || approvalStatus.isEmpty()) {
            setErrorResponse(approvalStatusService.getStatusCode(), approvalStatusService.getErrorMessage());
            return null;
        }

        return approvalRequest;
    }

    @Transactional
    public ApprovalRequestEntity createApprovalRequest(DocumentEntity document, UserEntity creator) {
        try {
            ApprovalRequestEntity approvalRequest = ApprovalRequestEntity.builder()
                    .documentId(document.getId())
                    .userId(creator.getId())
                    .status(0L)
                    .build();

            return approvalRequestRepository.save(approvalRequest);
        } catch (Exception e) {
            setErrorResponse(HttpStatus.BAD_REQUEST,"신청결재 저장 중 오류가 발생했습니다: " + e.getMessage());
            return null;
        }
    }

    public DocumentInfoModel getApprovalRequestByApprovalId(Long approvalId) {
        DocumentInfoModel document = documentService.getDocumentByApprovalId(approvalId);
        if(document == null) {
            setErrorResponse(documentService.getStatusCode(), documentService.getErrorMessage());
            return null;
        }
        return document;
    }
}
