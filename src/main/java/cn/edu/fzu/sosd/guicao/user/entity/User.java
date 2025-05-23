package cn.edu.fzu.sosd.guicao.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "username")
    private String username;

    @TableField(value = "password")
    private String password;

    @TableField(value = "student_id")
    private String studentId;

    @TableField(value = "dormitory")
    private String dormitory;

    @TableField(value = "college")
    private String college;

    @TableField(value = "major")
    private String major;

    @TableField(value = "email")
    private String email;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "avatar")
    private String avatar;

    @TableField(value = "gender")
    private Integer gender;

    @TableField(value = "points")
    private Integer points;

    @TableField(value = "status")
    private Integer status;

    @TableField(value = "last_login_time")
    private Date lastLoginTime;

    @TableField(value = "last_login_ip")
    private String lastLoginIp;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(value = "deleted")
    @TableLogic
    private Integer deleted;
}
