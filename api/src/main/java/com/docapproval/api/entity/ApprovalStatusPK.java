package com.docapproval.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ApprovalStatusPK implements Serializable {
    @Column(name = "approval_request_id")
    private Long approvalRequestId;

    @Column(name = "user_id")
    private Long userId;
}
