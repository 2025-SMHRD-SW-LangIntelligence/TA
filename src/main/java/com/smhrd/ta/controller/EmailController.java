package com.smhrd.ta.controller;

import com.smhrd.ta.service.EmailService;
import com.smhrd.ta.service.EmailVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private EmailService emailService;

    // 이메일 인증 메일 보내기
    @PostMapping("/send-verification-email")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        try {
            String token = emailVerificationService.generateToken(email);
            String verificationUrl = "http://localhost:8088/email/verify?token=" + token;

            emailService.sendEmail(email, "이메일 인증", "아래 링크를 클릭하여 인증하세요:\n" + verificationUrl);

            return ResponseEntity.ok("인증 메일을 발송했습니다.");
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에 에러 출력
            return ResponseEntity.status(500).body("인증 메일 발송에 실패했습니다. 관리자에게 문의하세요.");
        }
    }


    // 이메일 인증 토큰 검증
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        boolean verified = emailVerificationService.verifyToken(token);
        if (verified) {
            return ResponseEntity.ok("이메일 인증 성공!");
        } else {
            return ResponseEntity.badRequest().body("이메일 인증 실패 또는 유효하지 않은 토큰입니다.");
        }
    }
}

