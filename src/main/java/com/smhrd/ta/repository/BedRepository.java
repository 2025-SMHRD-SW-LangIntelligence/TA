package com.smhrd.ta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.ta.entity.BedEntity;

@Repository
public interface BedRepository extends JpaRepository<BedEntity, String> {
	List<BedEntity> findByBedId(String bedId);
} 