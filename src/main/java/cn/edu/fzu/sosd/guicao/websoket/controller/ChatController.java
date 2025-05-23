package cn.edu.fzu.sosd.guicao.websoket.controller;

import cn.edu.fzu.sosd.guicao.websoket.entity.ChatMessage;
import cn.edu.fzu.sosd.guicao.websoket.service.ChatMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(ChatMessage message) {
        // 设置发送时间
        message.setCreateTime(new Date());

        // 存储到数据库
        chatMessageService.saveMessage(message);

        // 广播给当前宿舍群聊的所有用户
        String destination = "/topic/messages/" + message.getDormitoryId();
        messagingTemplate.convertAndSend(destination, message);
    }
}
