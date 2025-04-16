package com.docapproval.api.dto.apikey;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiKeyDto {
    @Schema(description = "API Key 고유 ID", example = "1")
    private Long id;

    @Schema(description = "API Key를 사용하는 클라이언트 이름", example = "external-client")
    private String name;

    @Schema(description = "API Key 값", example = "sk_abcdefgh12345678")
    private String key;

    @Schema(description = "활성화 여부", example = "true")
    private Boolean active;

    @Schema(description = "생성 시각", example = "2025-04-16T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "만료 시각 (선택사항)", example = "2025-12-31T23:59:59", nullable = true)
    private LocalDateTime expiresAt;
    
}
