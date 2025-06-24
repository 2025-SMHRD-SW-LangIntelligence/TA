package com.smhrd.ta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smhrd.ta.entity.OccurEntity;
import com.smhrd.ta.repository.OccurRepository;

@Service
public class OccurService {

	@Autowired
	OccurRepository occurRepository;

	public List<OccurEntity> findFilteredWithinBounds(
			String swLat,
			String neLat,
			String swLng,
			String neLng,
			List<String> regions,
			List<String> years,
			List<String> statuses
	) {
//		return occurRepository.findWithFilters(swLat, neLat, swLng, neLng);
		return occurRepository.findByLatitudeBetweenAndLongitudeBetween(swLat, neLat, swLng, neLng);
	}

}
