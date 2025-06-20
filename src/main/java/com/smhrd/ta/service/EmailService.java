package com.smhrd.ta.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String email) {
        String subject = "이메일 인증을 완료해주세요.";
        String token = "생성된토큰"; // 실제 구현에서는 토큰 생성 로직
        String verificationUrl = "http://localhost:8088/email/verify?token=" + token;

        String htmlContent = "<p>아래 링크를 클릭하여 이메일 인증을 완료해주세요:</p>"
                + "<p><a href=\"" + verificationUrl + "\">이메일 인증하기</a></p>"
                + "<p>만약 링크가 작동하지 않는다면, 아래 URL을 복사해서 브라우저에 붙여넣으세요:</p>"
                + "<p>" + verificationUrl + "</p>";

        this.sendEmail(email, subject, htmlContent);
    }

    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // ✅ 넘겨받은 HTML 내용 사용

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 실패: " + e.getMessage(), e);
        }
    }


}
