package com.smhrd.ta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smhrd.ta.entity.EmailVerification;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    // ❌ 삭제 또는 더 이상 사용하지 않기
    // Optional<EmailVerification> findByEmail(String email);

    // ✅ 이메일로 검색하여 가장 최근 생성된 인증정보 1개만 가져오기
    Optional<EmailVerification> findTopByEmailOrderByCreatedAtDesc(String email);

    Optional<EmailVerification> findByToken(String token);

    List<EmailVerification> findAllByEmail(String email);
}
