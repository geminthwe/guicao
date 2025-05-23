package cn.edu.fzu.sosd.guicao.user.dto;

import lombok.Data;

@Data
public class CheckPhoneCodeReq {
    String code;
    String phone;
    private String password;
    private String confirmPassword;
}
