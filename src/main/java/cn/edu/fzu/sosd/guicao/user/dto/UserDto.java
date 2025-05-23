package cn.edu.fzu.sosd.guicao.user.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String email;
    private String studentId;
    private String dormitory;
    private String college;
    private String major;
    private String phone;
    private String avatar;
    private String gender;
    private Integer points;
}
