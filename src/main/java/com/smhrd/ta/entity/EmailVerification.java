package com.smhrd.ta.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String token;

    private boolean verified;

    private LocalDateTime createdAt;

    // 생성자
    public EmailVerification() {}

    public EmailVerification(String email, String token) {
        this.email = email;
        this.token = token;
        this.verified = false;
        this.createdAt = LocalDateTime.now();
    }

    // Getter / Setter
    public Long getId() { return id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public boolean isVerified() { return verified; }

    public void setVerified(boolean verified) { this.verified = verified; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // 필요시: 만료시간 체크 메서드
    public boolean isExpired() {
        return createdAt.plusMinutes(30).isBefore(LocalDateTime.now());
    }
}
