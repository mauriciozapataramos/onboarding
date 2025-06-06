package com.jmzr.onboarding.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jmzr.onboarding.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);
}