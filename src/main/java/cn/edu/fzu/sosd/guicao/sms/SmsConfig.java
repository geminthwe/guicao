package cn.edu.fzu.sosd.guicao.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {
    @Value("${sms.config.AccessKeyID}")
    private String accessKeyId;

    @Value("${sms.config.AccessKeySecret}")
    private String accessKeySecret;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }
}
