package cn.edu.fzu.sosd.guicao.websoket.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("chat_message")
public class ChatMessage {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("sender_id")
    private Long senderId;
    @TableField("dormitory_id")
    private Long dormitoryId;
    @TableField("content")
    private String content;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}