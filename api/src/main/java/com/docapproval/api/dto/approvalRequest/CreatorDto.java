package com.docapproval.api.dto.approvalRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreatorDto {

    @Schema(description = "신청자 id (사용자 id)")
    private Long id;

    @Schema(description = "신청자명 (사용자명)")
    private String name;
}
