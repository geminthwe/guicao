package cn.edu.fzu.sosd.guicao.user.dto;

import lombok.Data;

@Data
public class LoginReq {
    private String phone;
    private String password;
}
