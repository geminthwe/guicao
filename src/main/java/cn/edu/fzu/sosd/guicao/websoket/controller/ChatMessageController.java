package cn.edu.fzu.sosd.guicao.websoket.controller;

import cn.edu.fzu.sosd.guicao.websoket.entity.ChatMessage;
import cn.edu.fzu.sosd.guicao.websoket.service.ChatMessageService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    /**
     * 获取特定宿舍的历史消息（带分页）
     * @param dormitoryId 宿舍ID
     * @param currentPage 当前页码
     * @param pageSize 每页大小
     * @return 历史消息列表（带分页信息）
     */
    @GetMapping("/history")
    public Page<ChatMessage> getHistoryMessages(@RequestParam Long dormitoryId,
                                                @RequestParam(defaultValue = "1") Integer currentPage,
                                                @RequestParam(defaultValue = "10") Integer pageSize) {
        return chatMessageService.getHistoryMessagesByDormitory(dormitoryId, currentPage, pageSize);
    }
}
