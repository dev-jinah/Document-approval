package com.docapproval.api.repository.custom;

import com.docapproval.api.entity.ApprovalRequestEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRequestRepositoryCustom {
    List<ApprovalRequestEntity> findMyOngoingDocuments(Long userId);

    List<ApprovalRequestEntity> findCompletedApprovalDocuments(Long userId, List<Long> approvalRequestIds);

    List<ApprovalRequestEntity> findOngoingApprovalRequests();
}
