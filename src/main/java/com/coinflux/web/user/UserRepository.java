package com.coinflux.web.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE UserEntity u SET u.activatedAt = CURRENT_TIMESTAMP WHERE u.email = :email AND u.activatedAt IS NULL")
    int activateUser(@Param("email") String email);

    boolean existsByEmailAndActivatedAtIsNotNull(String email);
}