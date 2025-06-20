package com.smhrd.ta.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Report_img")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Img_id")
    private Long imgId;

    @Column(name = "ImgUrl")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "report_id")
    @JsonIgnoreProperties("images") // 순환 참조 방지
    @ToString.Exclude
    private Report report;



    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setReport(Report report) {
        this.report = report;
    }

}
