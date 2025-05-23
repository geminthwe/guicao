package cn.edu.fzu.sosd.guicao.user.service;

import java.util.concurrent.ExecutionException;

public interface PhoneValidCodeService {
    boolean checkPhone(String phone, String code);

    void send(String phone) throws ExecutionException, InterruptedException;
}
