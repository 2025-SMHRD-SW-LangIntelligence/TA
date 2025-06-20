package com.smhrd.ta.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.smhrd.ta.entity.Report;
import com.smhrd.ta.entity.ReportImg;
import com.smhrd.ta.service.ReportService;

import jakarta.servlet.http.HttpSession;

import com.smhrd.ta.entity.User;
import com.smhrd.ta.repository.ReportRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private ReportService reportService;

	// 게시글 목록 보기
	@GetMapping("/list")
	public String listReports(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String keyword, Model model) {

		Page<Report> reportPage;

		if (keyword != null && !keyword.trim().isEmpty()) {
			reportPage = reportService.searchPagedReports(keyword, page, size);
			model.addAttribute("keyword", keyword);
		} else {
			reportPage = reportService.getPagedReports(page, size);
		}

		model.addAttribute("reports", reportPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", reportPage.getTotalPages());

		return "reportList";
	}

	// 게시글 상세보기
	@GetMapping("/detail/{id}")
	public String showDetail(@PathVariable("id") Long id, Model model) {
		Report report = reportService.findById(id); // 이 메서드는 서비스에 있어야 함
		model.addAttribute("report", report);
		return "reportDetail"; // 보여줄 HTML 템플릿 (reportDetail.html)
	}

	// 일반 검색 (뷰 반환)
	@GetMapping("/search")
	public String searchReports(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
		List<Report> searchResults;

		if (keyword == null || keyword.trim().isEmpty()) {
			searchResults = reportService.findAllReports();
		} else {
			searchResults = reportService.searchReports(keyword);
		}

		model.addAttribute("reports", searchResults);
		model.addAttribute("keyword", keyword);
		return "reportList";
	}

	// AJAX용 JSON 검색 (경로 분리 필수!)
	@GetMapping(value = "/search/json", produces = "application/json")
	@ResponseBody
	public List<Map<String, Object>> searchReportsAjax(@RequestParam("type") String type,
			@RequestParam("keyword") String keyword) {

		List<Report> results;

		switch (type) {
		case "id":
			results = reportRepository.findByIdContaining(keyword);
			break;
		case "location":
			results = reportRepository.findByLocationContaining(keyword);
			break;
		case "content":
		default:
			results = reportRepository.findByContentContaining(keyword);
			break;
		}

		// Report -> 마스킹된 Map으로 변환
		return results.stream().map(Report::toMaskedJson).collect(Collectors.toList());
	}

	// 본인 계정 외 삭제 제한
	@PostMapping("/delete/{id}")
	public String deleteReport(@PathVariable("id") Long id, HttpSession session,
			RedirectAttributes redirectAttributes) {
		User loginUser = (User) session.getAttribute("loginUser");
		Report report = reportService.findById(id);

		// 서버에서도 본인 확인
		if (report != null && loginUser != null && report.getId().equals(loginUser.getUsername())) {
			reportService.deleteReport(id);
			redirectAttributes.addFlashAttribute("message", "삭제되었습니다.");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "삭제 권한이 없습니다.");
		}

		return "redirect:/report/list";
	}

	// 본인 외 수정 제한
	@GetMapping("/edit/{id}")
	public String editReportForm(@PathVariable("id") Long id, HttpSession session, Model model) {
		Report report = reportService.findById(id);

		// 로그인한 사용자와 작성자가 다르면 접근 제한
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null || !loginUser.getUsername().equals(report.getId())) {
			return "redirect:/report/list";
		}

		model.addAttribute("report", report);
		return "reportEditForm"; // 수정용 html
	}

	@PostMapping("/update")
	public String updateReport(@ModelAttribute Report report, @RequestParam("file") MultipartFile file,
			HttpSession session, RedirectAttributes redirectAttributes) {

		// 1. 로그인 사용자 정보 가져오기
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
			return "redirect:/login";
		}

		// 2. 기존 게시글 가져오기
		Report existingReport = reportService.findById(report.getReportId());

		// 3. 작성자 본인인지 확인
		if (!existingReport.getId().equals(loginUser.getUsername())) {
			redirectAttributes.addFlashAttribute("errorMessage", "작성자만 수정할 수 있습니다.");
			return "redirect:/report/detail/" + report.getReportId();
		}

		// 4. 기존 내용 수정
		existingReport.setContent(report.getContent());
		existingReport.setLocation(report.getLocation());
		existingReport.setWriteDay(LocalDate.now());

		// 5. 새 파일이 있다면 저장
		if (!file.isEmpty()) {
			try {
				String uploadPath = "C:/upload/";
				String originalName = file.getOriginalFilename();
				String savedName = UUID.randomUUID().toString() + "_" + originalName;

				file.transferTo(new File(uploadPath + savedName));

				ReportImg img = new ReportImg();
				img.setImgUrl("/upload/" + savedName);
				existingReport.getImages().clear(); // 기존 이미지 제거
				existingReport.addImage(img);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 6. 저장
		reportService.saveReport(existingReport);

		return "redirect:/report/detail/" + report.getReportId();
	}

	// 비회원 유저 게시글 작성 제한.

	@GetMapping("/new")
	public String showForm(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
			return "redirect:/login";
		}

		model.addAttribute("report", new Report());
		return "reportForm"; // reportForm.html
	}

	// 게시글 저장 처리 (파일 업로드 포함)
	@PostMapping("/save")
	public String saveReport(@ModelAttribute Report report, @RequestParam("file") MultipartFile file,
			HttpSession session) throws IOException {
		System.out.println("saveReport 메서드 진입");

		// 로그인한 사용자 확인
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		report.setId(loginUser.getUsername());

		report.setWriteDay(LocalDate.now());

		// 이미지 저장
		if (!file.isEmpty()) {
			String originalName = file.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			String savedName = uuid + "_" + originalName;

			// 실제 저장
			String uploadPath = "C:/upload/"; // 절대경로로 변경

			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				boolean created = uploadDir.mkdirs();
				System.out.println("upload 폴더 생성 여부: " + created);
			}

			file.transferTo(new File(uploadPath + savedName));

			// 이미지 객체 생성 및 연결
			ReportImg reportImg = new ReportImg();
			reportImg.setImgUrl("/upload/" + savedName);

			report.addImage(reportImg); // 양방향 관계 자동 설정
		}

		// 저장
		reportService.saveReport(report);
		return "redirect:/report/list";
	}

	// 페이징 적용 10개씩 보기
	@GetMapping("/reportList")
	public String reportList(@RequestParam(defaultValue = "0") int page, Model model) {
		int pageSize = 10;
		Page<Report> reportsPage = reportService.getPagedReports(page);

		model.addAttribute("reportsPage", reportsPage);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", reportsPage.getTotalPages());

		return "reportList";
	}

}
