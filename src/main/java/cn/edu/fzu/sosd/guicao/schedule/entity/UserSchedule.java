package cn.edu.fzu.sosd.guicao.schedule.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;

@Data
@TableName("user_schedule")
public class UserSchedule {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("title")
    private String title;

    @TableField("start_time")
    private LocalTime startTime;

    @TableField("end_time")
    private LocalTime endTime;

    @TableField("date")
    private Date date;

    @TableField("type")
    private String type; // 类型（class, custom）
}