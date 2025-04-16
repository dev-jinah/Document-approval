package com.docapproval.api.repository;

import com.docapproval.api.entity.ApprovalRequestEntity;
import com.docapproval.api.repository.custom.ApprovalRequestRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequestEntity, Long>, ApprovalRequestRepositoryCustom {
    ApprovalRequestEntity findByDocumentId(Long documentId);
}
