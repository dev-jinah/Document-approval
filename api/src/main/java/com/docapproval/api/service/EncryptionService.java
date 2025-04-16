package com.docapproval.api.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class EncryptionService extends BaseService {
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String ENCRYPTION_KEY = "jinah-encryption-key";

    public String getEncrypted(String value) {
        try {
            // 키를 해시하여 비밀키 생성
            byte[] key = getHashedKey(ENCRYPTION_KEY);

            // Cipher 객체 생성 및 초기화
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            // 암호화
            byte[] encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));

            // Base64로 인코딩하여 반환
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            setErrorResponse(HttpStatus.BAD_REQUEST,"암호화 진행 중 오류가 발생했습니다: " + e.getMessage());
            return null;
        }
    }

    private static byte[] getHashedKey(String key) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = digest.digest(key.getBytes(StandardCharsets.UTF_8));
        return hashedBytes;
    }
}
