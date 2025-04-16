package com.docapproval.api.repository.impl;

import com.docapproval.api.entity.ApprovalStatusEntity;
import com.docapproval.api.entity.QApprovalStatusEntity;
import com.docapproval.api.repository.custom.ApprovalStatusRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApprovalStatusRepositoryImpl implements ApprovalStatusRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QApprovalStatusEntity approvalStatusEntity;

    public ApprovalStatusRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
        this.approvalStatusEntity = QApprovalStatusEntity.approvalStatusEntity;
    }
    @Override
    public List<ApprovalStatusEntity> findByApprovalRequestId(Long approvalRequestId) {
        return queryFactory
                .selectFrom(approvalStatusEntity)
                .where(
                        approvalStatusEntity.approvalStatusPK.approvalRequestId.eq(approvalRequestId)
                )
                .orderBy(approvalStatusEntity.step.asc())
                .fetch();
    }

    @Override
    public List<Long> findApprovalRequestIdByCompletedAndUserId(Long userId) {
        return queryFactory
                .from(approvalStatusEntity)
                .select(approvalStatusEntity.approvalStatusPK.approvalRequestId)
                .where(
                        approvalStatusEntity.approvalStatusPK.userId.eq(userId)
                                .and(approvalStatusEntity.status.in(2,3))
                )
                .fetch();
    }


}
