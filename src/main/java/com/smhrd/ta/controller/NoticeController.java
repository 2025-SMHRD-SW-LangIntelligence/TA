package com.smhrd.ta.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger; // 추가
import org.slf4j.LoggerFactory; // 추가
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.ta.entity.NotionAnnouncement;
import com.smhrd.ta.service.NotionService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/announcements")
public class NoticeController {

    private static final Logger log = LoggerFactory.getLogger(NoticeController.class); // 추가

    private final NotionService notionService;

    public NoticeController(NotionService notionService) {
        this.notionService = notionService;
        log.info("NoticeController가 성공적으로 초기화되었습니다."); // 추가
    }

    @GetMapping
    public Mono<ResponseEntity<List<NotionAnnouncement>>> getAnnouncements() {
        log.info("GET /api/announcements 요청을 받았습니다."); // 추가
        return notionService.getAnnouncements()
                .map(announcements -> {
                    List<NotionAnnouncement> filteredAnnouncements = announcements.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                    log.info("총 {}개의 공지사항을 반환합니다.", filteredAnnouncements.size()); // 추가
                    return ResponseEntity.ok(filteredAnnouncements);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(e -> {
                    log.error("공지사항을 가져오는 중 오류 발생: {}", e.getMessage(), e); // 수정: System.err -> log.error
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    // mapToAnnouncementDto 메서드는 새로운 Entity 구성에서 필요 없습니다.
    // NotionAnnouncement 클래스 자체에 mapToAnnouncement() 메서드가 포함되어 있으므로
    // 이 메서드는 삭제하거나 주석 처리해야 합니다.
    // 현재 NotionAnnouncement::mapToAnnouncement를 직접 사용하고 있으므로, 이 메서드는 사용되지 않을 것입니다.
    // 만약 이 메서드가 아직 남아있다면, 삭제하거나 주석 처리하여 혼란을 방지하는 것이 좋습니다.
    /*
    private NotionAnnouncement mapToAnnouncementDto(NotionAnnouncement notionPage) {
        NotionAnnouncement dto = new NotionAnnouncement(); // 이제 DTO가 아니라 Entity 자신을 사용
        dto.setId(notionPage.getId());

        // 1. Title 속성 추출
        NotionAnnouncement.PropertyValue titlePropValue = notionPage.getProperties().get("Title");
        if (titlePropValue != null && titlePropValue.getTitle() != null && !titlePropValue.getTitle().isEmpty()) {
            dto.setTitle(titlePropValue.getTitle().get(0).getPlainText());
        } else {
            dto.setTitle("[제목 없음]");
        }

        // 2. Contents 속성 추출
        NotionAnnouncement.PropertyValue contentsPropValue = notionPage.getProperties().get("Contents");
        if (contentsPropValue != null && contentsPropValue.getRichText() != null && !contentsPropValue.getRichText().isEmpty()) {
            String fullContents = contentsPropValue.getRichText().stream()
                    .map(NotionAnnouncement.PropertyValue.TextElement::getPlainText)
                    .collect(Collectors.joining("\n"));
            dto.setContents(fullContents);
        } else {
            dto.setContents("[내용 없음]");
        }

        // 3. Date 속성 추출
        NotionAnnouncement.PropertyValue datePropValue = notionPage.getProperties().get("Date");
        if (datePropValue != null && datePropValue.getDate() != null && datePropValue.getDate().getStart() != null) {
            dto.setDate(datePropValue.getDate().getStart());
        } else {
            dto.setDate("[날짜 없음]");
        }

        return dto;
    }
    */
}