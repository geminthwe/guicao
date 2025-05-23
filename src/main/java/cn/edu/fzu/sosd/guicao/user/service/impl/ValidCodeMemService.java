package cn.edu.fzu.sosd.guicao.user.service.impl;

import cn.edu.fzu.sosd.guicao.email.Email;
import cn.edu.fzu.sosd.guicao.email.EmailService;
import cn.edu.fzu.sosd.guicao.email.builder.ValidateCodeEmailBuilder;
import cn.edu.fzu.sosd.guicao.user.service.ValidCodeService;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ValidCodeMemService implements ValidCodeService {

    private static final Logger log = LoggerFactory.getLogger(ValidCodeMemService.class);
    private final Map<String, ValidCode> storage = new ConcurrentHashMap<>();
    private final ExecutorService cleaner = Executors.newScheduledThreadPool(1);

    @Autowired
    private EmailService emailService;

    @PostConstruct
    public void init() {
        initCleaner();
    }

    @Override
    public boolean check(String email, String code) {
        ValidCode validCode = storage.get(email);
        if (validCode == null) {
            System.out.println("checkValidCode: null");
            return false;
        }
        System.out.println("checkValidCode: " + validCode);
        if (validCode.expireTime < System.currentTimeMillis()) {
            storage.remove(email);
            return false;
        }
        return validCode.code.equals(code);
    }

    @Override
    public void send(String email) {
        ValidCode validCode = new ValidCode();
        String code = RandomStringUtils.randomNumeric(4);
        long validTime = 1000 * 60 * 5; // 5 minutes
        validCode.code = code;
        validCode.expireTime = System.currentTimeMillis() + validTime;
        storage.put(email, validCode);

        Email sent = new ValidateCodeEmailBuilder(code, validTime).build(email);
        emailService.send(sent);
    }

    void initCleaner() {
        cleaner.execute(() -> {
            while (true) {
                try {
                    // each hour
                    Thread.sleep(1000 * 60 * 60);
                    long now = System.currentTimeMillis();
                    storage.entrySet().removeIf(entry -> entry.getValue().expireTime < now);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.error("cleaner error: ", e);
                }
            }
        });
    }

    static class ValidCode {
        public String code;
        public long expireTime;
    }

}
