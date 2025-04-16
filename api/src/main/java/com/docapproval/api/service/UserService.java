package com.docapproval.api.service;

import com.docapproval.api.dto.approvalRequest.CreatorDto;
import com.docapproval.api.dto.login.LoginCreateDto;
import com.docapproval.api.entity.UserEntity;
import com.docapproval.api.repository.UserRepository;
import com.docapproval.api.response.user.LoginModel;
import com.docapproval.api.response.user.UserModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService extends BaseService {

    private final UserRepository userRepository;

    private final EncryptionService encryptionService;

    public UserService(
            UserRepository userRepository,
            EncryptionService encryptionService)
    {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
    }

    public LoginModel getUserInfoOrCreate(LoginCreateDto loginCreateDto) {
        if(loginCreateDto.getName() == null || loginCreateDto.getName().trim().length() == 0) {
            setErrorResponse(HttpStatus.BAD_REQUEST, "사용자명을 입력해주세요.");
            return null;
        }
        if(loginCreateDto.getPassword() == null || loginCreateDto.getPassword().trim().length() == 0) {
            setErrorResponse(HttpStatus.BAD_REQUEST, "패스워드를 입력해주세요.");
            return null;
        }
        String name = loginCreateDto.getName().trim();
        String password = loginCreateDto.getPassword().trim();

        LoginModel loginInfo = null;
        UserEntity userInfo = getUserByName(name);
        String token = encryptionService.getEncrypted(name);
        if (userInfo == null) {
            // 사용자 정보가 없으면 새로 생성
            userInfo = createUser(loginCreateDto);
            loginInfo = new LoginModel(userInfo, true, token);
        } else {
            // 패스워드 암호화
            String encryptedPassword = encryptionService.getEncrypted(password);
            // 패스워드 다르면 이미 존재하는 사용자 명인거 확인
            if(!userInfo.getPassword().equals(encryptedPassword)) {
                setErrorResponse(HttpStatus.BAD_REQUEST, "패스워드가 다르지만 이름이 동일한 사용자가 존재합니다.");
                return null;
            }
            loginInfo = new LoginModel(userInfo, false, token);
        }
        return loginInfo;
    }

    @Transactional
    public UserEntity createUser(LoginCreateDto loginCreateDto) {
        if(loginCreateDto.getName() == null || loginCreateDto.getName().trim().length() == 0) {
            setErrorResponse(HttpStatus.BAD_REQUEST, "사용자명을 입력해주세요.");
            return null;
        }
        if(loginCreateDto.getPassword() == null || loginCreateDto.getPassword().trim().length() == 0) {
            setErrorResponse(HttpStatus.BAD_REQUEST, "패스워드를 입력해주세요.");
            return null;
        }

        String name = loginCreateDto.getName().trim();
        String password = loginCreateDto.getPassword().trim();

        UserEntity userInfo = getUserByName(name);
        if(userInfo != null) {
            setErrorResponse(HttpStatus.BAD_REQUEST, "이름이 동일한 사용자가 존재합니다.");
            return null;
        }

        // 패스워드 암호화
        String encryptedPassword = encryptionService.getEncrypted(password);

        UserEntity user = UserEntity.builder()
                .name(name)
                .password(encryptedPassword)
                .build();

        return userRepository.save(user);
    }

    public UserEntity getUserInfo(CreatorDto creator) {
        UserEntity userInfo = null;
        Optional<Long> idOptional = Optional.ofNullable(creator.getId());
        Optional<String> nameOptional = Optional.ofNullable(creator.getName());
        if (idOptional.isPresent() && idOptional.get() != 0) {
            userInfo = getUserById(Long.valueOf(creator.getId()));
        } else if (nameOptional.isPresent() && !nameOptional.get().isEmpty()) {
            userInfo = getUserByName(creator.getName());
        }
        return userInfo;
    }

    public UserEntity getUserById(Long id) {
        UserEntity userInfo = null;
        userInfo = userRepository.findById(id).orElse(null);
        if (userInfo == null) {
            setErrorResponse(HttpStatus.NOT_FOUND, "사용자 정보가 존재하지 않습니다.");
            return null;
        }
        return userInfo;
    }

    public UserEntity getUserByName(String name) {
        UserEntity userInfo = null;
        userInfo = userRepository.findByName(name);
        if (userInfo == null) {
            setErrorResponse(HttpStatus.NOT_FOUND, "사용자 정보가 존재하지 않습니다.");
            return null;
        }
        return userInfo;
    }

    public List<UserModel> getAllUsers() {
        List<UserModel> users = new ArrayList<>();
        for (UserEntity user : userRepository.findAll()) {
            users.add(new UserModel(user));
        }
        return users;
    }
}
