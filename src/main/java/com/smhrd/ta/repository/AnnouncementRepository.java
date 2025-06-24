// src/main/java/com/miku/demo/repository/AnnouncementRepository.java
package com.smhrd.ta.repository;

import com.smhrd.ta.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    // 최신순으로 정렬하여 페이징 (PostDate를 기준으로 내림차순 정렬)
    // Spring Data JPA는 메서드 이름으로 쿼리를 자동으로 생성합니다.
    Page<Announcement> findAllByOrderByPostDateDesc(Pageable pageable);
}