package com.docapproval.api.repository.impl;

import com.docapproval.api.entity.ApprovalRequestEntity;
import com.docapproval.api.entity.QApprovalRequestEntity;
import com.docapproval.api.entity.QApprovalStatusEntity;
import com.docapproval.api.entity.QDocumentEntity;
import com.docapproval.api.repository.custom.ApprovalRequestRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApprovalRequestRepositoryImpl implements ApprovalRequestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QApprovalRequestEntity approvalRequestEntity;

    private final QDocumentEntity documentEntity;

    private final QApprovalStatusEntity approvalStatusEntity;

    public ApprovalRequestRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
        this.approvalRequestEntity = QApprovalRequestEntity.approvalRequestEntity;
        this.documentEntity = QDocumentEntity.documentEntity;
        this.approvalStatusEntity = QApprovalStatusEntity.approvalStatusEntity;
    }

    @Override
    public List<ApprovalRequestEntity> findMyOngoingDocuments(Long userId) {
        // 결재 신청에서 userId 상태 중 0(대기),1(진행)
        return queryFactory
                .selectFrom(approvalRequestEntity)
                .where(
                        approvalRequestEntity.userId.eq(userId)
                                .and(approvalRequestEntity.status.in(0,1))
                ).orderBy(approvalRequestEntity.documentId.desc())
                .fetch();
    }

    @Override
    public List<ApprovalRequestEntity> findCompletedApprovalDocuments(Long userId, List<Long> approvalRequestIds) {
        // 신청자, 또는 결재자인 결재가 완료(승인 또는 거절)된 문서
        return queryFactory
                .selectFrom(approvalRequestEntity)
                .innerJoin(approvalRequestEntity.document, documentEntity)
                .where(
                        (approvalRequestEntity.userId.eq(userId)
                                .or(approvalRequestEntity.id.in(approvalRequestIds)))
                                .and(approvalRequestEntity.status.in(2,3))
                ).orderBy(approvalRequestEntity.documentId.desc())
                .fetch();
    }

    @Override
    public List<ApprovalRequestEntity> findOngoingApprovalRequests() {
        return queryFactory
                .selectFrom(approvalRequestEntity)
                .where(
                        approvalRequestEntity.status.in(0, 1)
                ).orderBy(approvalRequestEntity.documentId.desc())
                .fetch();
    }
}
