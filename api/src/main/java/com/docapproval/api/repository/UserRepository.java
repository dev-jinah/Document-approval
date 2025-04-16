package com.docapproval.api.repository;

import com.docapproval.api.entity.UserEntity;
import com.docapproval.api.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, UserRepositoryCustom {
    UserEntity findByName(String name);
}
