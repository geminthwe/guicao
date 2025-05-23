package cn.edu.fzu.sosd.guicao.user.dto;


import lombok.Data;

@Data
public class CreateUserReq {
    private String password;
    private String confirmPassword;
    private String phone;
    private String code;
}
