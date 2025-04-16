package com.docapproval.api.repository;

import com.docapproval.api.entity.DocumentEntity;
import com.docapproval.api.repository.custom.DocumentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long>, DocumentRepositoryCustom {
}
