package com.docapproval.api.repository;

import com.docapproval.api.entity.ApprovalStatusEntity;
import com.docapproval.api.entity.ApprovalStatusPK;
import com.docapproval.api.repository.custom.ApprovalStatusRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalStatusRepository extends JpaRepository<ApprovalStatusEntity, ApprovalStatusPK>, ApprovalStatusRepositoryCustom {
}
