package cn.edu.fzu.sosd.guicao.schedule.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("task")
public class Task {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("description")
    private String description;

    @TableField("area")
    private String area;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
