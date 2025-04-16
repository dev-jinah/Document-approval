package com.docapproval.api.entity;

import com.docapproval.api.dto.document.DocumentCreateDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "create_time", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime;

    public DocumentEntity(DocumentCreateDto documentCreateDto) {
        this.title = documentCreateDto.getTitle();
        this.category = documentCreateDto.getCategory();
        this.content = documentCreateDto.getContent();
        this.createTime = LocalDateTime.now();
    }
}
