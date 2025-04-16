package com.docapproval.api.service;

import com.docapproval.api.dto.document.DocumentCreateDto;
import com.docapproval.api.entity.ApprovalRequestEntity;
import com.docapproval.api.entity.DocumentEntity;
import com.docapproval.api.entity.UserEntity;
import com.docapproval.api.repository.ApprovalRequestRepository;
import com.docapproval.api.repository.DocumentRepository;
import com.docapproval.api.response.approvalRequest.ApprovalRequestModel;
import com.docapproval.api.response.approvalStatus.ApprovalStatusModel;
import com.docapproval.api.response.document.DocumentInfoModel;
import com.docapproval.api.response.document.DocumentModel;
import com.docapproval.api.response.user.UserModel;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DocumentService extends BaseService {

    private final DocumentRepository documentRepository;

    private final UserService userService;

    private final ApprovalStatusService approvalStatusService;

    private final ApprovalRequestRepository approvalRequestRepository;

    public DocumentService(
            DocumentRepository documentRepository, UserService userService, ApprovalStatusService approvalStatusService, ApprovalRequestRepository approvalRequestRepository
    ) {
        this.documentRepository = documentRepository;
        this.userService = userService;
        this.approvalStatusService = approvalStatusService;
        this.approvalRequestRepository = approvalRequestRepository;
    }

    @Transactional
    public DocumentEntity createDocument(DocumentCreateDto documentCreateDto) {
        try {
            DocumentEntity document = new DocumentEntity(documentCreateDto);
            return documentRepository.save(document);
        } catch (Exception e) {
            setErrorResponse(HttpStatus.BAD_REQUEST, "문서 저장 중 오류가 발생했습니다: " + e.getMessage());
            return null;
        }
    }

    // OUTBOX: 내가 생성한 문서 중 결재 진행 중인 문서 by user ID
    public Set<DocumentInfoModel> getMyOngoingDocumentsByUserId(Long userId) {

        UserEntity user = userService.getUserById(userId);
        if (user == null) {
            setErrorResponse(userService.getStatusCode(), userService.getErrorMessage());
            return null;
        }
        return getMyOngoingDocuments(user);
    }

    // OUTBOX: 내가 생성한 문서 중 결재 진행 중인 문서 by user Name
    public Set<DocumentInfoModel> getMyOngoingDocumentsByUserName(String userName) {

        UserEntity user = userService.getUserByName(userName);
        if (user == null) {
            setErrorResponse(userService.getStatusCode(), userService.getErrorMessage());
            return null;
        }
        return getMyOngoingDocuments(user);
    }

    // INBOX: 내가 결재를 해야 할 문서 by user ID
    public Set<DocumentInfoModel> getDocumentsForApprovalByUserId(Long userId) {
        UserEntity user = userService.getUserById(userId);
        if (user == null) {
            setErrorResponse(userService.getStatusCode(), userService.getErrorMessage());
            return null;
        }
        return getDocumentsForApproval(user);
    }

    // INBOX: 내가 결재를 해야 할 문서 by user Name
    public Set<DocumentInfoModel> getDocumentsForApprovalByUserName(String userName) {

        UserEntity user = userService.getUserByName(userName);
        if (user == null) {
            setErrorResponse(userService.getStatusCode(), userService.getErrorMessage());
            return null;
        }
        return getDocumentsForApproval(user);
    }

    // ARCHIVE: 내가 관여한 문서 중 결재가 완료(승인 또는 거절)된 문서 by user ID
    public Set<DocumentInfoModel> getCompletedApprovalDocumentsByUserId(Long userId) {

        UserEntity user = userService.getUserById(userId);
        if (user == null) {
            setErrorResponse(userService.getStatusCode(), userService.getErrorMessage());
            return null;
        }
        return getCompletedApprovalDocuments(user);
    }

    // ARCHIVE: 내가 관여한 문서 중 결재가 완료(승인 또는 거절)된 문서 by user Name
    public Set<DocumentInfoModel> getCompletedApprovalDocumentsByUserName(String userName) {

        UserEntity user = userService.getUserByName(userName);
        if (user == null) {
            setErrorResponse(userService.getStatusCode(), userService.getErrorMessage());
            return null;
        }
        return getCompletedApprovalDocuments(user);
    }

    // OUTBOX: 내가 생성한 문서 중 결재 진행 중인 문서
    private Set<DocumentInfoModel> getMyOngoingDocuments(UserEntity user) {
        try {
            List<ApprovalRequestEntity> approvalRequestEntityList = approvalRequestRepository.findMyOngoingDocuments(user.getId());

            return getApprovalRequests(approvalRequestEntityList);
        } catch (Exception e) {
            // 예외 발생 시 처리
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "내가 생성한 문서 중 결재 진행 중인 문서 조회 중 오류가 발생했습니다: " + e.getMessage());
            return null;
        }
    }

    // INBOX: 내가 결재를 해야 할 문서
    private Set<DocumentInfoModel> getDocumentsForApproval(UserEntity user) {
        List<ApprovalRequestEntity> approvalRequestEntityList = new ArrayList<>();
        try {
            // 결재 진행중인 결재신청
            List<ApprovalRequestEntity> ongoingApprovalRequests = approvalRequestRepository.findOngoingApprovalRequests();

            ongoingApprovalRequests.forEach(approvalRequestEntity -> {
                if (approvalStatusService.isApprovalTargetsByUserId(approvalRequestEntity.getId(), user.getId()) != null) {
                    approvalRequestEntityList.add(approvalRequestEntity);
                }
            });

            return getApprovalRequests(approvalRequestEntityList);
        } catch (Exception e) {
            // 예외 발생 시 처리
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "내가 결재를 해야 할 문서 조회 중 오류가 발생했습니다: " + e.getMessage());
            return null;
        }
    }

    // ARCHIVE: 내가 관여한 문서 중 결재가 완료(승인 또는 거절)된 문서
    private Set<DocumentInfoModel> getCompletedApprovalDocuments(UserEntity user) {
        try {
            List<Long> approvalRequestIds = approvalStatusService.getApprovalRequestIdByCompletedAndUserId(user.getId());

            List<ApprovalRequestEntity> approvalRequestEntityList = approvalRequestRepository.findCompletedApprovalDocuments(user.getId(), approvalRequestIds);

            return getApprovalRequests(approvalRequestEntityList);
        } catch (Exception e) {
            // 예외 발생 시 처리
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "내가 관여한 문서 중 결재가 완료(승인 또는 거절)된 문서 조회 중 오류가 발생했습니다: " + e.getMessage());
            return null;
        }
    }

    // 문서 전체 조회
    public Set<DocumentInfoModel> getAllDocumentInfo() {
        List<ApprovalRequestEntity> approvalRequestList = approvalRequestRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        if (approvalRequestList.size() == 0) {
            return new HashSet<>();
        }
        return getApprovalRequests(approvalRequestList);
    }

    public DocumentInfoModel getDocumentByDocumentId(Long documentId) {
        ApprovalRequestEntity approvalRequest = approvalRequestRepository.findByDocumentId(documentId);
        if (approvalRequest == null) {
            setErrorResponse(HttpStatus.NOT_FOUND, "문서 정보가 존재하지 않습니다.");
            return null;
        }
        return getApprovalRequest(approvalRequest);
    }

    public DocumentInfoModel getDocumentByApprovalId(Long approvalId) {
        ApprovalRequestEntity approvalRequest = approvalRequestRepository.findById(approvalId).orElse(null);
        if (approvalRequest == null) {
            setErrorResponse(HttpStatus.NOT_FOUND, "결재 신청 정보가 존재하지 않습니다.");
            return null;
        }
        return getApprovalRequest(approvalRequest);
    }

    private Set<DocumentInfoModel> getApprovalRequests(List<ApprovalRequestEntity> approvalRequestEntityList) {
        Set<DocumentInfoModel> approvalRequestModels = new HashSet<>();

        approvalRequestEntityList.forEach(approvalReq -> {
            approvalRequestModels.add(getApprovalRequest(approvalReq));
        });

        return approvalRequestModels;
    }

    private DocumentInfoModel getApprovalRequest(ApprovalRequestEntity approvalRequestEntity) {

        // 결재신청
        ApprovalRequestModel approvalRequest = new ApprovalRequestModel(approvalRequestEntity);

        // 문서
        DocumentModel document = new DocumentModel(approvalRequestEntity.getDocument());

        // 신청자
        UserModel creator = new UserModel(approvalRequestEntity.getUser());

        // 결재상황
        List<ApprovalStatusModel> approvalStatusList = approvalStatusService.approvalStatusListByApprovalRequestId(approvalRequestEntity.getId());

        return new DocumentInfoModel(approvalRequest, document, creator, approvalStatusList);
    }
}
