package com.smhrd.ta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smhrd.ta.entity.BedEntity;
import com.smhrd.ta.repository.BedRepository;

@Service
public class BedService {

	@Autowired
	BedRepository bedRepository;
	
	public List<BedEntity> findByBedId(String bedId) {
		return bedRepository.findByBedId(bedId);
	}
} 