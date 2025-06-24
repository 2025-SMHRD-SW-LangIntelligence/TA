package com.smhrd.ta.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonManagedReference;



@Entity
@Table(name = "`Report`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReportId")
    private Long reportId;

    @Column(name = "Content")
    private String content;

    @Column(name = "WriteDay")   // DB 컬럼명이 일치
    private LocalDate writeDay;

    @Column(name = "ID")
    private String id;

    @Column(name = "Location")
    private String location;
    
 // 파일 경로를 저장할 필드 추가
    private String imgPath;

    // Getter and Setter 추가
    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }


    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ReportImg> images = new ArrayList<>();


    public void setWriteDay(LocalDate now) {
        this.writeDay = now;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    public String getContent() {
        return content;
    }

    
    public Long getReportId() {
        return reportId;
    }
    
    public String getId() {
        return id;
    }
    
    public LocalDate getWriteDay() {
        return writeDay;
    }

    public String getLocation() {
        return location;
    }




    

    public void setImages(List<ReportImg> imageList) {
        this.images = imageList;
        for (ReportImg img : imageList) {
            img.setReport(this);
        }
    }

    public void addImage(ReportImg img) {
        images.add(img);
        img.setReport(this);
    }
    
    
    // 작성자 아이디 마스킹해서 가리기 
    public Map<String, Object> toMaskedJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("reportId", this.reportId);
        map.put("content", this.content);
        map.put("writeDay", this.writeDay);
        map.put("id", getMaskedId()); // ← 마스킹된 ID
        map.put("location", this.location);
        map.put("imgPath", this.imgPath);
        return map;
    }
    
    public String getMaskedId() {
        if (id == null || id.length() < 5) return "***";
        return id.substring(0, 3) + "***" + id.substring(id.length() - 2);
    }

    

}
