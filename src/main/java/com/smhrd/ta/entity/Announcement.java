// src/main/java/com/miku/demo/entity/Announcement.java
package com.smhrd.ta.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "announcements") // 데이터베이스 테이블명
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT") // 내용이 길 수 있으므로 TEXT 타입으로 지정
    private String content;

    @Column(nullable = false)
    private LocalDate postDate; // 공지 등록일

    // 기본 생성자 (JPA 요구사항)
    public Announcement() {
    }

    // 모든 필드를 포함하는 생성자 (선택 사항)
    public Announcement(String title, String content, LocalDate postDate) {
        this.title = title;
        this.content = content;
        this.postDate = postDate;
    }

    // Getter 및 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDate postDate) {
        this.postDate = postDate;
    }

    // toString (선택 사항)
    @Override
    public String toString() {
        return "Announcement{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", postDate=" + postDate +
               '}';
    }
}