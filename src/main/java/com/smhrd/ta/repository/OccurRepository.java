package com.smhrd.ta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smhrd.ta.entity.OccurEntity;

@Repository
public interface OccurRepository extends JpaRepository<OccurEntity, String> {
	@Query("SELECT o FROM OccurEntity o " +
		       "WHERE o.latitude BETWEEN :swLat AND :neLat " +
		       "AND o.longitude BETWEEN :swLng AND :neLng " +
		       "AND o.sido IN :regions " +      
		       "AND o.sagoDateYear IN :years " +
		       "AND o.trStatus IN :statuses " +
		       "AND o.sinkDepth BETWEEN :depth_min AND :depth_max")
		List<OccurEntity> findWithFilters(
		        @Param("swLat") String swLat,
		        @Param("neLat") String neLat,
		        @Param("swLng") String swLng,
		        @Param("neLng") String neLng,
		        @Param("regions") List<String> regions,
		        @Param("years") List<String> years,
		        @Param("statuses") List<String> statuses,
		        @Param("depth_min") Double depth_min,
		        @Param("depth_max") Double depth_max
		);
}
