package cn.edu.fzu.sosd.guicao.cv.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvaluationResult {
    @JsonProperty("cleanliness_score")
    private double cleanlinessScore;

    @JsonProperty("avg_before")
    private double avgBefore;

    @JsonProperty("avg_after")
    private double avgAfter;

    @JsonProperty("details")
    private EvaluationDetails details;
}
