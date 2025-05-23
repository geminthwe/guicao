package cn.edu.fzu.sosd.guicao.schedule.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;

@Data
@TableName("schedule")
public class Schedule {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("task_id")
    private Long taskId;

    @TableField("start_time")
    private LocalTime startTime;

    @TableField("end_time")
    private LocalTime endTime;

    @TableField("status")
    private Integer status; // 0-未完成，1-已完成

    @TableField("points")
    private Integer points;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField("date")
    private Date date;
}
