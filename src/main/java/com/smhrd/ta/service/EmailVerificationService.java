package com.smhrd.ta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smhrd.ta.entity.EmailVerification;
import com.smhrd.ta.repository.EmailVerificationRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;



@Service
public class EmailVerificationService {

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    public String generateToken(String email) {
        String newToken = UUID.randomUUID().toString();

        Optional<EmailVerification> optional = emailVerificationRepository.findTopByEmailOrderByCreatedAtDesc(email);


        if (optional.isPresent()) {
            EmailVerification ev = optional.get();
            // 기존 토큰 무효화 및 새로운 토큰 생성
            ev.setToken(newToken);
            ev.setVerified(false);
            ev.setCreatedAt(LocalDateTime.now());
            emailVerificationRepository.save(ev);
        } else {
            EmailVerification ev = new EmailVerification();
            ev.setEmail(email);
            ev.setToken(newToken);
            ev.setVerified(false);
            ev.setCreatedAt(LocalDateTime.now());
            emailVerificationRepository.save(ev);
        }

        return newToken;
    }

    public boolean verifyToken(String token) {
        Optional<EmailVerification> optional = emailVerificationRepository.findByToken(token);

        if (optional.isPresent()) {
            EmailVerification ev = optional.get();

            // 토큰이 최신인지 확인: 이메일로 다시 조회해서 비교
         // 최신 토큰인지 확인: 이메일로 최신 인증 기록 조회
            Optional<EmailVerification> latestRecordOpt = emailVerificationRepository.findTopByEmailOrderByCreatedAtDesc(ev.getEmail());

            if (latestRecordOpt.isPresent() && latestRecordOpt.get().getToken().equals(token)) {
                // 최신 토큰 맞음 → 인증 완료 처리
                ev.setVerified(true);
                emailVerificationRepository.save(ev);
                return true;
            }

        }

        return false;
    }
}




