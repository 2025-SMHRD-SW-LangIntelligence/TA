package com.smhrd.ta.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "occur")
public class OccurEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String occurId;
	
    private String location;
	private Double latitude;
	private Double longitude;
    private String sagoDate;
    private Double sinkWidth;
    private Double sinkExtend;
    private Double sinkDepth;
	
	@Column(nullable = false)
    private String bedId;
    private String sagoDetail;
    private String sagoImgUrl;
    private String sagoNewsUrl;
    private Integer deathCnt;
    private Integer injuryCnt;
    private Integer vehicleCnt;
    private String trStatus;
    private String trMethod;
    private String trFnDate;
    
    private String sido;
    private String sagoDateYear;
}
