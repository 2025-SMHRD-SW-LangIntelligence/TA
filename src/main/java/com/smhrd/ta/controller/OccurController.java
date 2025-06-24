package com.smhrd.ta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.ta.entity.OccurEntity;
import com.smhrd.ta.service.OccurService;

@RestController
public class OccurController {

	@Autowired
	private OccurService occurService;

	@GetMapping("/api/occur/within")
	public List<OccurEntity> getOccurWithin(@RequestParam String swLat, @RequestParam String swLng,
			@RequestParam String neLat, @RequestParam String neLng) {

		return occurService.findWithinBounds(swLat, neLat, swLng, neLng);
	}

}
