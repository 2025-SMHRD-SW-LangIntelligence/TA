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
	
	@Column(nullable = false)
    private String location;
	private String latitude;
	private String longitude;
	
	@Column(nullable = false)
    private String sagoDate;
	
    private String sinkWidth;
    private String sinkExtend;
    private String sinkDepth;
	
	@Column(nullable = false)
    private String bedId;
    private String sagoDetail;
    private String sagoImgUrl;
    private String sagoNewsUrl;
    private String deathCnt;
    private String injuryCnt;
    private String vehicleCnt;
    private String trStatus;
    private String trMethod;
    private String trFnDate;
}
