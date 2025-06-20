package com.smhrd.ta.service;

import com.smhrd.ta.entity.NotionAnnouncement; // Entity 참조
import com.smhrd.ta.repository.NotionRepository; // Repository 참조
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotionService {

    private final NotionRepository notionRepository;

    @Autowired
    public NotionService(NotionRepository notionRepository) {
        this.notionRepository = notionRepository;
    }

    /**
     * Notion 데이터베이스에서 공지사항 목록을 조회하고, 필요한 필드만 추출하여 반환합니다.
     * @return NotionAnnouncement 엔티티 목록 (Title, Contents, Date가 파싱되어 있음)
     */
    public Mono<List<NotionAnnouncement>> getAnnouncements() {
        return notionRepository.findAllNotionPages() // Repository에서 Notion API raw 응답 가져오기
                .map(wrapper -> {
                    // Wrapper에서 NotionAnnouncement 목록을 가져와 각 엔티티를 파싱
                    return wrapper.getResults().stream()
                            .map(NotionAnnouncement::mapToAnnouncement) // NotionAnnouncement 엔티티 내의 mapToAnnouncement() 호출
                            .collect(Collectors.toList());
                });
    }
}