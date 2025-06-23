// src/main/java/com/miku/demo/service/AnnouncementService.java
package com.smhrd.ta.service;

import com.smhrd.ta.entity.Announcement;
import com.smhrd.ta.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    /**
     * 모든 공지사항을 페이징하여 가져옵니다. 최신 공지사항이 먼저 표시됩니다.
     * @param page 현재 페이지 번호 (0부터 시작)
     * @param size 한 페이지에 표시할 항목 수
     * @return 공지사항의 Page 객체
     */
    public Page<Announcement> getAllAnnouncements(int page, int size) {
        // PostDate를 기준으로 내림차순 정렬하여 Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size, Sort.by("postDate").descending());
        return announcementRepository.findAll(pageable);
    }

    /**
     * 특정 ID의 공지사항을 가져옵니다.
     * @param id 공지사항 ID
     * @return Optional<Announcement> (공지사항이 없을 수도 있으므로 Optional로 반환)
     */
    public Optional<Announcement> getAnnouncementById(Long id) {
        return announcementRepository.findById(id);
    }

    /**
     * 새로운 공지사항을 생성합니다.
     * @param announcement 생성할 공지사항 객체
     * @return 생성된 공지사항 객체
     */
    public Announcement createAnnouncement(Announcement announcement) {
        if (announcement.getPostDate() == null) {
            announcement.setPostDate(LocalDate.now()); // 등록일이 없으면 현재 날짜로 설정
        }
        return announcementRepository.save(announcement);
    }

    /**
     * 기존 공지사항을 업데이트합니다.
     * @param id 업데이트할 공지사항의 ID
     * @param updatedAnnouncement 업데이트할 내용이 담긴 공지사항 객체
     * @return 업데이트된 공지사항 객체
     * @throws RuntimeException 해당 ID의 공지사항을 찾을 수 없을 때 발생
     */
    public Announcement updateAnnouncement(Long id, Announcement updatedAnnouncement) {
        return announcementRepository.findById(id)
                .map(announcement -> {
                    announcement.setTitle(updatedAnnouncement.getTitle());
                    announcement.setContent(updatedAnnouncement.getContent());
                    // postDate는 수동으로 업데이트하지 않는 것이 일반적이지만, 필요하면 추가
                    // announcement.setPostDate(updatedAnnouncement.getPostDate());
                    return announcementRepository.save(announcement);
                })
                .orElseThrow(() -> new RuntimeException("Announcement not found with id " + id)); // 적절한 Custom Exception 사용 권장
    }

    /**
     * 특정 ID의 공지사항을 삭제합니다.
     * @param id 삭제할 공지사항 ID
     */
    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }
}