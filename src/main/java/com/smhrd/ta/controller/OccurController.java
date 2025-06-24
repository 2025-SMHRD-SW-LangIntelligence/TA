package com.smhrd.ta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.ta.entity.BedEntity;
import com.smhrd.ta.entity.OccurEntity;
import com.smhrd.ta.service.BedService;
import com.smhrd.ta.service.OccurService;

@RestController
public class OccurController {

	@Autowired
	private OccurService occurService;
	
	@Autowired
	private BedService bedService;

	@GetMapping("/api/occur/within")
	public List<OccurEntity> getOccurWithin(
			@RequestParam String swLat, 
			@RequestParam String swLng,
			@RequestParam String neLat, 
			@RequestParam String neLng,
			@RequestParam(required = false) List<String> regions,
			@RequestParam(required = false) List<String> years,
			@RequestParam(required = false) List<String> statuses,
			@RequestParam(required = false) Double depth_min,
			@RequestParam(required = false) Double depth_max
	) {
		return occurService.findFilteredWithinBounds(swLat, neLat, swLng, neLng, regions, years, statuses, depth_min, depth_max);
	}
	
	@GetMapping("/api/occur/bed")
	public List<BedEntity> getBed(@RequestParam String bedId){
		
		return bedService.findByBedId(bedId);
	}

}