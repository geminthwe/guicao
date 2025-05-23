package cn.edu.fzu.sosd.guicao.user.service.impl;

import cn.edu.fzu.sosd.guicao.sms.SmsService;
import cn.edu.fzu.sosd.guicao.user.service.PhoneValidCodeService;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PhoneCodeServiceImpl implements PhoneValidCodeService {

    private static final Logger log = LoggerFactory.getLogger(ValidCodeMemService.class);
    private final Map<String, ValidCodeMemService.ValidCode> storage = new ConcurrentHashMap<>();
    private final ExecutorService cleaner = Executors.newScheduledThreadPool(1);

    @Autowired
    private SmsService smsService;

    @PostConstruct
    public void init() {
        initCleaner();
    }

    @Override
    public boolean checkPhone(String phone, String code) {
        ValidCodeMemService.ValidCode validCode = storage.get(phone);
        if (validCode == null) {
            System.out.println("checkValidCode: null");
            return false;
        }
        System.out.println("checkValidCode: " + validCode);
        if (validCode.expireTime < System.currentTimeMillis()) {
            storage.remove(phone);
            return false;
        }
        return validCode.code.equals(code);
    }

    @Override
    public void send(String phone) throws ExecutionException, InterruptedException {
        ValidCodeMemService.ValidCode validCode = new ValidCodeMemService.ValidCode();
        String code = RandomStringUtils.randomNumeric(4);
        long validTime = 1000 * 60 * 5; // 5 minutes
        validCode.code = code;
        validCode.expireTime = System.currentTimeMillis() + validTime;
        storage.put(phone, validCode);

        smsService.send(phone, code);
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
