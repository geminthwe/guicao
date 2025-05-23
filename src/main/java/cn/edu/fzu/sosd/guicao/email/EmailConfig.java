package cn.edu.fzu.sosd.guicao.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {
    @Value("${email.config.host}")
    private String host;
    @Value("${email.config.port}")
    private int port;
    @Value("${email.config.sender}")
    private String sender;
    @Value("${email.config.password}")
    private String password;
    @Value("${email.config.protocol}")
    private String protocol;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getSender() {
        return sender;
    }

    public String getPassword() {
        return password;
    }

    public String getProtocol() {
        return protocol;
    }
}
