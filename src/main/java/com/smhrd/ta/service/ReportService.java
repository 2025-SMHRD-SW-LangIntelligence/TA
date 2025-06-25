package com.smhrd.ta.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.smhrd.ta.entity.Report;
import com.smhrd.ta.repository.ReportRepository;


@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public List<Report> findAllReports() {
        return reportRepository.findAll();
    }

    public void saveReport(Report report) {
        reportRepository.save(report);
    }

    public Report findById(Long id) {
        return reportRepository.findWithImagesByReportId(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
    }
    
 // 레포트 리스트 최신 등록순으로 보이기 - 처음 작성한 레포트 리스트 순서 1부터 시작
    public Page<Report> getPagedReports(int page) {
        int size = 10; // 혹은 상수로 지정
        Pageable pageable = PageRequest.of(page, size, Sort.by("reportId").descending());
        return reportRepository.findAll(pageable);
    }

    public Page<Report> searchPagedReports(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("reportId").descending());
        return reportRepository.findByContentContaining(keyword, pageable);
    }





    public void deleteById(Long id) {
        reportRepository.deleteById(id);
    }
    
    public List<Report> searchReports(String type, String keyword) {
        if ("id".equals(type)) {
            return reportRepository.findByIdContaining(keyword);
        } else if ("content".equals(type)) {
            return reportRepository.findByContentContaining(keyword);
        } else if ("location".equals(type)) {
            return reportRepository.findByLocationContaining(keyword);
        } else {
            return new ArrayList<>(); // 검색 조건이 일치하지 않으면 빈 리스트
        }
    }
    
    public List<Report> searchReports(String keyword) {
    	System.out.println("검색 키워드: " + keyword);
        return reportRepository.findByContentContainingIgnoreCase(keyword);
    }


	public static void write(Report report) {
		
	}

	public Page<Report> getPagedReports(int page, int size) {
	    Pageable pageable = PageRequest.of(page, size, Sort.by("reportId").descending());
	    return reportRepository.findAll(pageable);
	}
	
	public void deleteReport(Long id) {
        reportRepository.deleteById(id);  // ✅ 실제 삭제 수행
    }
	

}
