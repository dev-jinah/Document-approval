package com.docapproval.api.repository.custom;

import com.docapproval.api.entity.ApprovalStatusEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalStatusRepositoryCustom {
    List<ApprovalStatusEntity> findByApprovalRequestId(Long approvalRequestId);

    List<Long> findApprovalRequestIdByCompletedAndUserId(Long userId);
}
