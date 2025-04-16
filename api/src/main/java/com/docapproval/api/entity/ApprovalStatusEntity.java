package com.docapproval.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "approval_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalStatusEntity {
    @EmbeddedId
    private ApprovalStatusPK approvalStatusPK;

    @Column(name = "step", nullable = false)
    private Long step;

    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "status")
    private Long status;

    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
