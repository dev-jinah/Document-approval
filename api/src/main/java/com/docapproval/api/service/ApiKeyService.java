package com.docapproval.api.service;

import com.docapproval.api.entity.ApiKeyEntity;
import com.docapproval.api.repository.ApiKeyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApiKeyService extends BaseService {

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    /**
     * API Key 유효성 검증
     */
    public boolean validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, "API Key가 필요합니다.");
            return false;
        }

        Optional<ApiKeyEntity> apiKeyEntity = apiKeyRepository.findByKey(apiKey);

        if (apiKeyEntity.isEmpty()) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, "유효하지 않은 API Key입니다.");
            return false;
        }

        if (!apiKeyEntity.get().isValid()) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, "API Key가 비활성화되었거나 만료되었습니다.");
            return false;
        }

        return true;
    }

    /**
     * API Key 조회
     */
    public ApiKeyEntity getApiKeyByKey(String key) {
        return apiKeyRepository.findByKey(key).orElse(null);
    }

    /**
     * API Key 생성
     */
    public ApiKeyEntity createApiKey(ApiKeyEntity apiKeyEntity) {
        try {
            return apiKeyRepository.save(apiKeyEntity);
        } catch (Exception e) {
            setErrorResponse(HttpStatus.BAD_REQUEST, "API Key 생성 중 오류가 발생했습니다: " + e.getMessage());
            return null;
        }
    }
}
