package cn.edu.fzu.sosd.guicao.cv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EvaluationDetails {
    @JsonProperty("sharpness_change")
    private double sharpnessChange;
    @JsonProperty("blob_decrease")
    private int blobDecrease;
    @JsonProperty("texture_simplified")
    private double textureSimplified;
    @JsonProperty("color_similarity")
    private double colorSimilarity;
}
