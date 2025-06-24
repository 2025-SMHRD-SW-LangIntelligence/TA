package com.smhrd.ta.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "bed")
public class BedEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String bedId;
	private String bedType;
	private String bedInfo;
}