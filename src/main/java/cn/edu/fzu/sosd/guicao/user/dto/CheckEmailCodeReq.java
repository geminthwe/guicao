package cn.edu.fzu.sosd.guicao.user.dto;

import lombok.Data;

@Data
public class CheckEmailCodeReq {
    String code;
    String email;
    private String password;
    private String confirmPassword;
}
