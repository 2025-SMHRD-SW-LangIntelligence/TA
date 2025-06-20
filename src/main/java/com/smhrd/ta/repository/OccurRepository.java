package com.smhrd.ta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.ta.entity.OccurEntity;

@Repository
public interface OccurRepository extends JpaRepository<OccurEntity, String> {
	List<OccurEntity> findByLatitudeBetweenAndLongitudeBetween(String latStart, String latEnd, String lngStart,
			String lngEnd);

}
