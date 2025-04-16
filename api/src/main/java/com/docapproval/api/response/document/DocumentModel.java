package com.docapproval.api.response.document;

import com.docapproval.api.entity.DocumentEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DocumentModel {

    @Schema(description = "문서 Id")
    private final Long id;

    @Schema(description = "문서의 제목")
    private final String title;

    @Schema(description = "문서의 분류")
    private final String category;

    @Schema(description = "문서의 내용")
    private final String content;

    @Schema(description = "문서 생성 일시")
    private final LocalDateTime createTime;

    public DocumentModel(DocumentEntity documentEntity) {
        this.id = documentEntity.getId();
        this.title = documentEntity.getTitle();
        this.category = documentEntity.getCategory();
        this.content = documentEntity.getContent();
        this.createTime = documentEntity.getCreateTime();
    }
}
