// src/main/java/com/miku/demo/controller/AnnouncementController.java
package com.smhrd.ta.controller;

import com.smhrd.ta.entity.Announcement;
import com.smhrd.ta.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller // 이 클래스가 웹 요청을 처리하는 컨트롤러임을 나타냅니다.
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Autowired // Spring이 AnnouncementService의 인스턴스를 자동으로 주입합니다.
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

   
    /**
     * "/announcements" 경로 요청을 처리하여 공지사항 목록 페이지를 반환합니다.
     * @param page 현재 페이지 번호 (기본값: 0)
     * @param size 한 페이지당 공지사항 수 (기본값: 10)
     * @param model 뷰로 데이터를 전달하는 Model 객체
     * @return "announce" (src/main/resources/templates/announce.html)
     */
    @GetMapping("/announcements")
    public String getAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        // 서비스 계층에서 공지사항 데이터를 페이징하여 가져옵니다.
        Page<Announcement> announcementPage = announcementService.getAllAnnouncements(page, size);

        // 뷰(HTML)에서 사용할 데이터를 Model에 추가합니다.
        model.addAttribute("announcements", announcementPage.getContent()); // 현재 페이지의 공지사항 목록
        model.addAttribute("currentPage", announcementPage.getNumber());    // 현재 페이지 번호
        model.addAttribute("totalPages", announcementPage.getTotalPages()); // 전체 페이지 수
        model.addAttribute("totalItems", announcementPage.getTotalElements());// 전체 공지사항 수
        model.addAttribute("size", size); // 페이지당 항목 수

        return "announce"; // "announce.html" 템플릿을 찾아 렌더링합니다.
    }

    /**
     * "/announcements/{id}" 경로 요청을 처리하여 특정 공지사항의 상세 페이지를 반환합니다.
     * @param id 조회할 공지사항의 ID
     * @param model 뷰로 데이터를 전달하는 Model 객체
     * @return "announcementDetail" (상세 페이지 템플릿) 또는 "error/404"
     */
    @GetMapping("/announcements/{id}")
    public String getAnnouncementDetail(@PathVariable Long id, Model model) {
        Optional<Announcement> announcement = announcementService.getAnnouncementById(id);
        if (announcement.isPresent()) {
            model.addAttribute("announcement", announcement.get());
            // 공지사항 상세 내용을 표시할 announcementDetail.html 파일이 필요합니다.
            return "announcementDetail";
        } else {
            // 해당 ID의 공지사항이 없을 경우 404 에러 페이지를 반환하거나 다른 처리를 할 수 있습니다.
            return "error/404"; // 예: src/main/resources/templates/error/404.html
        }
    }
}