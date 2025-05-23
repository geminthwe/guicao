package cn.edu.fzu.sosd.guicao.schedule.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;

@Data
@TableName("dormitory_task")
public class DormitoryTask {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String dormitory;

    private Long taskId;

    // 每天固定时间执行
    private LocalTime startTime;
    private LocalTime endTime;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}
