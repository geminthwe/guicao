package cn.edu.fzu.sosd.guicao.user.dto;

import lombok.Data;

@Data
public class UpdatePwReq {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
