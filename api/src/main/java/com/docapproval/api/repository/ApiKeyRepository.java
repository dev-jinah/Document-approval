package com.docapproval.api.repository;

import com.docapproval.api.entity.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {
    Optional<ApiKeyEntity> findByKey(String key);
    Optional<ApiKeyEntity> findByName(String name);
}
