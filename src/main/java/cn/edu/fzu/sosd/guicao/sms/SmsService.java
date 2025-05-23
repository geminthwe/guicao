package cn.edu.fzu.sosd.guicao.sms;

import java.util.concurrent.ExecutionException;

public interface SmsService {
    void send(String phoneNumber,String code) throws ExecutionException, InterruptedException;
}
