package com.docapproval.api.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DocumentCreateDto {

    @Schema(description = "문서의 제목")
    private String title;

    @Schema(description = "문서의 분류")
    private String category;

    @Schema(description = "문서의 내용")
    private String content;
}
