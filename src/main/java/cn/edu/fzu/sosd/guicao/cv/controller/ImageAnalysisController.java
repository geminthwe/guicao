package cn.edu.fzu.sosd.guicao.cv.controller;



import cn.edu.fzu.sosd.guicao.cv.dto.AnalysisResult;
import com.aliyun.apache.hc.client5.http.classic.methods.HttpPost;
import com.aliyun.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import com.aliyun.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import com.aliyun.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import com.aliyun.apache.hc.client5.http.impl.classic.HttpClients;
import com.aliyun.apache.hc.core5.http.ContentType;
import com.aliyun.apache.hc.core5.http.ParseException;
import com.aliyun.apache.hc.core5.http.io.entity.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ImageAnalysisController {

    private static final String PYTHON_SERVICE_URL = "http://localhost:5000/evaluate";
    private static final ObjectMapper objectMapper = new ObjectMapper(); // 用于 JSON 解析

    @PostMapping("/analyze")
    public AnalysisResult analyzeImages(@RequestParam("beforeImage") MultipartFile beforeImage,
                                        @RequestParam("afterImage") MultipartFile afterImage) throws IOException {
        File beforeFile = convertToFile(beforeImage, "before.jpg");
        File afterFile = convertToFile(afterImage, "after.jpg");

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost uploadFile = new HttpPost(PYTHON_SERVICE_URL);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.addBinaryBody(
                    "beforeImage",
                    beforeFile,
                    ContentType.DEFAULT_BINARY,
                    beforeFile.getName()
            );

            builder.addBinaryBody(
                    "afterImage",
                    afterFile,
                    ContentType.DEFAULT_BINARY,
                    afterFile.getName()
            );

            uploadFile.setEntity(builder.build());
            try (CloseableHttpResponse response = httpClient.execute(uploadFile)) {
                String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");

                // 将 JSON 转换为 AnalysisResult 对象
                AnalysisResult result = objectMapper.readValue(jsonResponse, AnalysisResult.class);
                return result;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } finally {
            // 删除临时文件
            if (!beforeFile.delete() || !afterFile.delete()) {
                System.err.println("Failed to delete temporary file.");
            }
        }
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }
}