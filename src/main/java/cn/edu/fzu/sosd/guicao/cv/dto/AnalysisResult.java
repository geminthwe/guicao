package cn.edu.fzu.sosd.guicao.cv.dto;

import lombok.Data;

@Data
public class AnalysisResult {

    private double cleanliness_score;

    private String assessment_result;

    private double change_area_ratio;
}
