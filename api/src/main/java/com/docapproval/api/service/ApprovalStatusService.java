package com.docapproval.api.service;

import com.docapproval.api.dto.approvalRequest.ApprovalStepDto;
import com.docapproval.api.dto.approvalStatus.ApprovalStatusUpdateDto;
import com.docapproval.api.dto.login.LoginCreateDto;
import com.docapproval.api.entity.*;
import com.docapproval.api.repository.ApprovalRequestRepository;
import com.docapproval.api.repository.ApprovalStatusRepository;
import com.docapproval.api.response.approvalStatus.ApprovalStatusModel;
import com.docapproval.api.response.user.UserModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApprovalStatusService extends BaseService {

    private final UserService userService;

    private final ApprovalStatusRepository approvalStatusRepository;

    private final ApprovalRequestRepository approvalRequestRepository;

    public ApprovalStatusService(UserService userService, ApprovalStatusRepository approvalStatusRepository, ApprovalRequestRepository approvalRequestRepository) {
        this.userService = userService;
        this.approvalStatusRepository = approvalStatusRepository;
        this.approvalRequestRepository = approvalRequestRepository;
    }

    @Transactional
    public List<ApprovalStatusEntity> createApprovalStatusEntity(Long id, List<ApprovalStepDto> approvalStepList) {
        try {
            List<ApprovalStatusEntity> approvalStatusList = new ArrayList<>();
            Set<Long> approvalUserIdSet = new HashSet<>();
            Long step = 1L;
            for (ApprovalStepDto approvalStepDto : approvalStepList) {
                Long approvalUserId = approvalStepDto.getUserId();
                Optional<Long> idOptional = Optional.ofNullable(approvalStepDto.getUserId());
                Optional<String> nameOptional = Optional.ofNullable(approvalStepDto.getUserName());
                if ((!idOptional.isPresent() || idOptional.get() == 0) && nameOptional.isPresent() && !nameOptional.get().isEmpty()) {
                    UserEntity approvalUser = userService.getUserByName(approvalStepDto.getUserName());
                    if (approvalUser == null) {
                        // 결재자 정보 존재하지않으면 저장하기 (id, pw 동일)
                        LoginCreateDto loginCreateDto = new LoginCreateDto(approvalStepDto.getUserName(), approvalStepDto.getUserName());
                        UserEntity user = userService.createUser(loginCreateDto);
                        approvalUserId = user.getId();
                    } else {
                        approvalUserId = approvalUser.getId();
                    }
                }

                if (!approvalUserIdSet.add(approvalUserId)) {
                    setErrorResponse(HttpStatus.BAD_REQUEST, "중복 결재자가 존재합니다. 결재자 ID: " + approvalUserId);
                    return null;
                }

                ApprovalStatusPK statusPK = new ApprovalStatusPK(id, approvalUserId);

                ApprovalStatusEntity approvalStatus = ApprovalStatusEntity.builder()
                        .approvalStatusPK(statusPK)
                        .step(step++)
                        .updateTime(LocalDateTime.now())
                        .build();

                approvalStatusList.add(approvalStatusRepository.save(approvalStatus));
            }
            return approvalStatusList;
        } catch (Exception e) {
            setErrorResponse(HttpStatus.BAD_REQUEST, "결재 처리 저장 중 오류가 발생했습니다: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public ApprovalStatusEntity updateApprovalStatus(ApprovalStatusUpdateDto approvalStatusUpdateDto) {
        if(approvalStatusUpdateDto.getIsApproved() == null) {
            setErrorResponse(HttpStatus.BAD_REQUEST, "결재처리 여부를 확인해주세요.");
            return null;
        }
        try {
            Long approvalRequestId = approvalStatusUpdateDto.getApprovalRequestId();
            Long userId = approvalStatusUpdateDto.getUserId();

            if(approvalRequestId == null) {
                setErrorResponse(HttpStatus.BAD_REQUEST, "결재문서를 확인해주세요.");
                return null;
            }
            if(userId == null) {
                setErrorResponse(HttpStatus.BAD_REQUEST, "결재자를 확인해주세요.");
                return null;
            }
            // 결재 진행 가능 여부
            ApprovalRequestEntity approvalRequest = isApprovalAllowedByApprovalRequestId(approvalRequestId);
            if (approvalRequest == null) {
                return null;
            }
            // 결재 대상자 여부
            List<ApprovalStatusEntity> approvalStatusList = isApprovalTargetsByUserId(approvalRequestId, userId);
            if (approvalStatusList == null) {
                return null;
            }
            ApprovalStatusEntity approvalStatus = approvalStatusList.get(0);

            if (approvalStatusUpdateDto.getIsApproved()) {
                // 승인 2
                approvalStatus.setStatus(2L);

                // 결재 신청 상태 마지막이면 승인 2
                if (approvalStatusList.size() == 1) {
                    approvalRequest.setStatus(2L);
                } else {
                    // 결재 신청 상태 남아있으면 진행 1
                    approvalRequest.setStatus(1L);
                }

            } else {
                // 반려 3
                approvalStatus.setStatus(3L);
                approvalRequest.setStatus(3L);
            }
            approvalStatus.setReason(approvalStatusUpdateDto.getReason());
            approvalStatus.setUpdateTime(LocalDateTime.now());
            ApprovalStatusEntity newApprovalStatus = approvalStatusRepository.save(approvalStatus);

            approvalRequestRepository.save(approvalRequest);

            return newApprovalStatus;

        } catch (Exception e) {
            // 예외 발생 시 처리
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "결재 처리 업데이트 중 오류가 발생했습니다: " + e.getMessage());
            return null;
        }
    }

    // 결재가 허용되는지 여부를 판단
    private ApprovalRequestEntity isApprovalAllowedByApprovalRequestId(Long approvalRequestId) {

        ApprovalRequestEntity approvalRequest = approvalRequestRepository.findById(approvalRequestId).orElse(null);
        if (approvalRequest == null) {
            setErrorResponse(HttpStatus.NOT_FOUND, "신청결재 정보가 존재하지 않습니다.");
            return null;
        } else if (approvalRequest.getStatus() != 0 && approvalRequest.getStatus() != 1) {
            //대기(0), 진행(1), 승인(2), 반려(3)
            String statusValue = approvalRequest.getStatus() == 2 ? "승인" : "반려";
            setErrorResponse(HttpStatus.BAD_REQUEST, "결재를 진행할 수 없습니다. 신청결재 상태 : " + statusValue);
            return null;
        }

        return approvalRequest;
    }

    // 결재 대상자 여부
    public List<ApprovalStatusEntity> isApprovalTargetsByUserId(Long approvalRequestId, Long userId) {

        List<ApprovalStatusEntity> approvalStatusEntityList = approvalStatusRepository.findByApprovalRequestId(approvalRequestId);
        if (approvalStatusEntityList.isEmpty()) {
            setErrorResponse(HttpStatus.NOT_FOUND, "결재자가 존재하지 않습니다.");
            return null;
        }
        List<ApprovalStatusEntity> approvalStatusList = approvalStatusEntityList.stream().filter(x -> x.getStatus() == null).collect(Collectors.toList());
        ApprovalStatusEntity approvalStatus = approvalStatusList.get(0);
        if (approvalStatus.getApprovalStatusPK().getUserId() != userId) {
            setErrorResponse(HttpStatus.NOT_FOUND, "결재자가 아닙니다. 결재자 id: " + approvalStatus.getApprovalStatusPK().getUserId());
            return null;
        }

        return approvalStatusList;
    }

    public List<ApprovalStatusModel> approvalStatusListByApprovalRequestId(Long approvalRequestId) {
        List<ApprovalStatusModel> approvalStatusModelList = new ArrayList<>();

        List<ApprovalStatusEntity> approvalStatusEntityList = approvalStatusRepository.findByApprovalRequestId(approvalRequestId);

        approvalStatusEntityList.forEach(approvalStatus -> {
            UserEntity approvalUser = userService.getUserById(approvalStatus.getApprovalStatusPK().getUserId());
            approvalStatusModelList.add(new ApprovalStatusModel(approvalStatus, new UserModel(approvalUser)));
        });

        return approvalStatusModelList;
    }

    public List<Long> getApprovalRequestIdByCompletedAndUserId(Long userId) {
        return approvalStatusRepository.findApprovalRequestIdByCompletedAndUserId(userId);
    }
}
