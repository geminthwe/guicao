package cn.edu.fzu.sosd.guicao.cv.controller;

import cn.edu.fzu.sosd.guicao.cv.EvaluationReportGenerator;
import cn.edu.fzu.sosd.guicao.cv.dto.EvaluationResult;
import cn.edu.fzu.sosd.guicao.cv.dto.ImagePairRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/eval")
public class EvaluationController {

    private static final String PYTHON_SERVICE_URL = "http://localhost:5000/evaluate";

    @PostMapping("/upload")
    public String evaluateImages(@RequestParam("beforeImage") MultipartFile beforeImage,
                                                           @RequestParam("afterImage") MultipartFile afterImage) throws Exception {

        if (beforeImage.isEmpty() || afterImage.isEmpty()) {
            throw new IllegalArgumentException("Both images must be provided");
        }

        // 将MultipartFile转换为Base64编码字符串
        String beforeImageBase64 = encodeImageToBase64(beforeImage);
        String afterImageBase64 = encodeImageToBase64(afterImage);


        RestTemplate restTemplate = new RestTemplate();

        // 构造 JSON 请求体
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("before", beforeImageBase64);
        requestBody.put("after", afterImageBase64);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                PYTHON_SERVICE_URL,
                requestBody,
                String.class);

        ObjectMapper mapper = new ObjectMapper();
        EvaluationResult result = mapper.readValue(responseEntity.getBody(), EvaluationResult.class);

        return EvaluationReportGenerator.generateEvaluationReport(result);

    }

    private String encodeImageToBase64(MultipartFile file) throws IOException {
        return Base64.getEncoder().encodeToString(file.getBytes());
    }
}
