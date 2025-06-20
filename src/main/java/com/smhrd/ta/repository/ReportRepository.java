package com.smhrd.ta.repository;

import com.smhrd.ta.entity.Report;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByIdContaining(String id);         // 작성자 ID 검색
    List<Report> findByContentContaining(String content); // 내용 검색
    List<Report> findByLocationContaining(String location); // 위치 검색
	List<Report> findByContentContainingIgnoreCase(String keyword);
	@EntityGraph(attributePaths = "images")
	Optional<Report> findWithImagesByReportId(Long reportId);
	Page<Report> findByContentContaining(String keyword, Pageable pageable);  // 검색 시 페이징
    Page<Report> findAll(Pageable pageable); // 전체 리스트 페이징
    
	
}




